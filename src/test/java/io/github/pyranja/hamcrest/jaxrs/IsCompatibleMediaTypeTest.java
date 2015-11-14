package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

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
    matching(null);
  }

  @Theory
  public void shouldMatchOnEqualMediaType(final MediaType mime) throws Exception {
    assertThat(mime, matching(mime));
  }

  @Theory
  public void shouldMatchIfTypeMatchesAndSubtypeIsWildcard(final MediaType mime) throws Exception {
    assertThat(mime, matching(new MediaType(mime.getType(), MediaType.MEDIA_TYPE_WILDCARD)));
  }

  @Theory
  public void shouldMatchIfExpectingWildcard(final MediaType mime) throws Exception {
    assertThat(mime, matching(MediaType.WILDCARD_TYPE));
  }

  @Theory
  public void shouldRejectIfTypeNotMatching(final MediaType mime) throws Exception {
    assertThat(mime, not(matching(new MediaType("illegal", mime.getSubtype()))));
  }

  @Theory
  public void shouldRejectIfTotalMisMatch(final MediaType mime) throws Exception {
    assertThat(mime, not(matching(new MediaType("illegal", "type"))));
  }

  @Theory
  public void shouldMatchIfSubtypeSuffixMatches(final MediaType mime) throws Exception {
    // construct a mime with matching suffix
    final MediaType actual = new MediaType(mime.getType(), "test-data+" + mime.getSubtype());
    assertThat(actual, matching(mime));
  }

  @Theory
  public void shouldRejectIfExpectingSuffixedAndActualIsGeneralSubType(final MediaType mime) throws Exception {
    // construct a mime with extended suffix
    final MediaType suffixed = new MediaType(mime.getType(), "test-data+" + mime.getSubtype());
    assertThat(mime, not(matching(suffixed)));
  }

  @Theory
  public void shouldIncludeExpectedTypeInDescription(final MediaType expected) throws Exception {
    final IsCompatibleMediaType subject = matching(expected);
    final StringDescription description = new StringDescription();
    subject.describeTo(description);
    assertThat(description.toString(), containsString(expected.toString()));
  }

  @Theory
  public void shouldIncludeActualMimeStringInMismatchDescription(final MediaType actual) throws Exception {
    final IsCompatibleMediaType subject = matching(new MediaType("illegal", "type"));
    final StringDescription description = new StringDescription();
    subject.describeMismatch(actual, description);
    assertThat(description.toString(), containsString(actual.toString()));
  }

  private IsCompatibleMediaType matching(final MediaType expected) {
    return new IsCompatibleMediaType(expected);
  }
}