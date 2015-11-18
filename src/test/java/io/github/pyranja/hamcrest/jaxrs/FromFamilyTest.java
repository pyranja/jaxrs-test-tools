/*
 * Copyright (C) 2015 Chris Borckholder (chris.borckholder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;

import java.util.Objects;

import static io.github.pyranja.hamcrest.jaxrs.JaxrsMatchers.fromFamily;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(Theories.class)
public class FromFamilyTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailOnNullExpectation() throws Exception {
    fromFamily(null);
  }

  @Theory
  public void shouldMatchIfStatusBelongsToExpectedFamily(final Response.Status status, final Response.Status.Family family) throws Exception {
    assumeThat(status.getFamily(), equalTo(family));
    assertThat(status, fromFamily(family));
  }

  @Theory
  public void shouldRejectIfStatusDoesNotBelongToExpectedFamily(final Response.Status status, final Response.Status.Family family) throws Exception {
    assumeThat(status.getFamily(), not(equalTo(family)));
    assertThat(status, not(fromFamily(family)));
  }

  @Theory
  public void shouldIncludeExpectedFamilyInDescription(final Response.Status.Family family) throws Exception {
    final StringDescription description = new StringDescription();
    fromFamily(family).describeTo(description);
    assertThat(description.toString(), containsString(family.toString()));
  }

  @Theory
  public void shouldIncludeActualStatusCodeAndFamilyInMismatchDescription(final Response.Status status) throws Exception {
    final StringDescription description = new StringDescription();
    fromFamily(Response.Status.Family.OTHER).describeMismatch(status, description);
    assertThat(description.toString(), containsString(Objects.toString(status.getStatusCode())));
    assertThat(description.toString(), containsString(status.getFamily().toString()));
  }
}
