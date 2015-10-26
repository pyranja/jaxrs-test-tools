package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.when;

@RunWith(Theories.class)
public class ResponseStatusMatcherTest {

  @DataPoints
  public static final Response.Status[] statuses = Response.Status.values();

  private final StringDescription description = new StringDescription();

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullReference() throws Exception {
    subjectExpecting(null);
  }

  @Theory
  public void shouldIncludeExpectedReasonAndCodeInDescription(final Response.Status status) throws Exception {
    subjectExpecting(status).describeTo(description);
    assertThat(description.toString(), containsString(status.toString()));
    assertThat(description.toString(), containsString(Objects.toString(status.getStatusCode())));
  }

  @Theory
  public void shouldMatchIfResponseHasExpectedStatus(final Response.Status status) throws Exception {
    assertThat(subjectExpecting(status).matches(mockResponseWith(status)), equalTo(true));
  }

  @Theory
  public void shouldRejectResponseWithUnexpectedStatus(final Response.Status status, final Response.Status other) throws Exception {
    assumeThat(status, not(equalTo(other)));
    assertThat(subjectExpecting(status).matches(mockResponseWith(other)), equalTo(false));
  }

  @Theory
  public void shouldIncludeFoundReasonAndCodeInMismatchDescription(final Response.Status status, final Response.Status other) throws Exception {
    assumeThat(status, not(equalTo(other)));
    subjectExpecting(status).describeMismatch(mockResponseWith(other), description);
    assertThat(description.toString(), containsString(other.toString()));
    assertThat(description.toString(), containsString(Objects.toString(other.getStatusCode())));
  }

  private ResponseStatusMatcher subjectExpecting(final Response.Status expected) {
    return new ResponseStatusMatcher(expected);
  }

  private Response mockResponseWith(final Response.Status status) {
    final Response mock = Mockito.mock(Response.class);
    when(mock.getStatusInfo()).thenReturn(status);
    when(mock.getStatus()).thenReturn(status.getStatusCode());
    return mock;
  }
}
