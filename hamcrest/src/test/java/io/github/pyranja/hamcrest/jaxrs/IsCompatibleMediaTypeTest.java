/*
 * Copyright © 2015 Chris Borckholder (chris.borckholder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.pyranja.hamcrest.jaxrs;

import static io.github.pyranja.hamcrest.jaxrs.JaxrsMatchers.compatibleTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;

@RunWith(Theories.class)
public class IsCompatibleMediaTypeTest {

  @DataPoints
  public static final MediaType[] mediaTypes = new MediaType[] {
    MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_FORM_URLENCODED_TYPE,
    MediaType.TEXT_HTML_TYPE, MediaType.TEXT_PLAIN_TYPE, MediaType.TEXT_XML_TYPE,
    MediaType.MULTIPART_FORM_DATA_TYPE,
    new MediaType("image", "jpeg")
  };

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    compatibleTo(null);
  }

  @Theory
  public void shouldMatchOnEqualMediaType(final MediaType mime) throws Exception {
    assertThat(mime, compatibleTo(mime));
  }

  @Theory
  public void shouldMatchIfTypeMatchesAndSubtypeIsWildcard(final MediaType mime) throws Exception {
    assertThat(mime, compatibleTo(new MediaType(mime.getType(), MediaType.MEDIA_TYPE_WILDCARD)));
  }

  @Theory
  public void shouldMatchIfExpectingWildcard(final MediaType mime) throws Exception {
    assertThat(mime, compatibleTo(MediaType.WILDCARD_TYPE));
  }

  @Theory
  public void shouldRejectIfTypeNotMatching(final MediaType mime) throws Exception {
    assertThat(mime, not(compatibleTo(new MediaType("illegal", mime.getSubtype()))));
  }

  @Theory
  public void shouldRejectIfTotalMisMatch(final MediaType mime) throws Exception {
    assertThat(mime, not(compatibleTo(new MediaType("illegal", "type"))));
  }

  @Theory
  public void shouldMatchIfSubtypeSuffixMatches(final MediaType mime) throws Exception {
    // construct a mime with matching suffix
    final MediaType actual = new MediaType(mime.getType(), "test-data+" + mime.getSubtype());
    assertThat(actual, compatibleTo(mime));
  }

  @Theory
  public void shouldRejectIfExpectingSuffixedAndActualIsGeneralSubType(final MediaType mime) throws Exception {
    // construct a mime with extended suffix
    final MediaType suffixed = new MediaType(mime.getType(), "test-data+" + mime.getSubtype());
    assertThat(mime, not(compatibleTo(suffixed)));
  }

  @Theory
  public void shouldIncludeExpectedTypeInDescription(final MediaType expected) throws Exception {
    final StringDescription description = new StringDescription();
    compatibleTo(expected).describeTo(description);
    assertThat(description.toString(), containsString(expected.toString()));
  }

  @Theory
  public void shouldIncludeActualMimeStringInMismatchDescription(final MediaType actual) throws Exception {
    final StringDescription description = new StringDescription();
    compatibleTo(new MediaType("illegal", "type")).describeMismatch(actual, description);
    assertThat(description.toString(), containsString(actual.toString()));
  }
}
