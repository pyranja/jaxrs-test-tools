package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(Theories.class)
public class ResponseStatusFamilyMatcherTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullReference() throws Exception {
    new ResponseStatusFamilyMatcher(null);
  }

  @Theory
  public void shouldIncludeExpectedFamilyInDescription(final Response.Status.Family family) {
    final StringDescription description = new StringDescription();
    subjectExpecting(family).describeTo(description);
    assertThat(description.toString(), containsString(family.toString()));
  }

  @Theory
  public void shouldMatchIfResponseStatusIsInExpectedFamily(final Response.Status status) {
    assertThat(subjectExpecting(status.getFamily()).matches(MockResponse.with(status)), equalTo(true));
  }

  @Theory
  public void shouldRejectResponseWithStatusFromAnotherFamily(final Response.Status.Family expected, final Response.Status other) {
    assumeThat(expected, not(equalTo(other.getFamily())));
    assertThat(subjectExpecting(expected).matches(MockResponse.with(other)), equalTo(false));
  }

  @Theory
  public void shouldIncludeFoundStatusAndCodeInMismatchDescription(final Response.Status.Family expected, final Response.Status actual) {
    assumeThat(expected, not(equalTo(actual.getFamily())));
    final StringDescription description = new StringDescription();
    subjectExpecting(expected).describeMismatch(MockResponse.with(actual), description);
    assertThat(description.toString(), containsString(actual.getReasonPhrase()));
    assertThat(description.toString(), containsString(Objects.toString(actual.getStatusCode())));
  }

  private ResponseStatusFamilyMatcher subjectExpecting(final Response.Status.Family family) {
    return new ResponseStatusFamilyMatcher(family);
  }
}
