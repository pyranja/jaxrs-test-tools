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
public class ResponseStatusMatcherTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullReference() throws Exception {
    subjectExpecting(null);
  }

  @Theory
  public void shouldIncludeExpectedReasonAndCodeInDescription(final Response.Status status) throws Exception {
    final StringDescription description = new StringDescription();
    subjectExpecting(status).describeTo(description);
    assertThat(description.toString(), containsString(status.toString()));
    assertThat(description.toString(), containsString(Objects.toString(status.getStatusCode())));
  }

  @Theory
  public void shouldMatchIfResponseHasExpectedStatus(final Response.Status status) throws Exception {
    assertThat(subjectExpecting(status).matches(MockResponse.with(status)), equalTo(true));
  }

  @Theory
  public void shouldRejectResponseWithUnexpectedStatus(final Response.Status status, final Response.Status other) throws Exception {
    assumeThat(status, not(equalTo(other)));
    assertThat(subjectExpecting(status).matches(MockResponse.with(other)), equalTo(false));
  }

  @Theory
  public void shouldIncludeFoundReasonAndCodeInMismatchDescription(final Response.Status status, final Response.Status other) throws Exception {
    assumeThat(status, not(equalTo(other)));
    final StringDescription description = new StringDescription();
    subjectExpecting(status).describeMismatch(MockResponse.with(other), description);
    assertThat(description.toString(), containsString(other.toString()));
    assertThat(description.toString(), containsString(Objects.toString(other.getStatusCode())));
  }

  private ResponseStatusMatcher subjectExpecting(final Response.Status expected) {
    return new ResponseStatusMatcher(expected);
  }
}
