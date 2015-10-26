package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.ws.rs.core.Response;
import java.util.Objects;

final class ResponseStatusMatcher extends TypeSafeDiagnosingMatcher<Response> {
  private final Response.Status expected;

  public ResponseStatusMatcher(final Response.Status expected) {
    this.expected = Objects.requireNonNull(expected);
  }

  @Override
  protected boolean matchesSafely(final Response item, final Description mismatchDescription) {
    if (item.getStatus() == expected.getStatusCode()) {
      return true;
    }
    mismatchDescription.appendValue(item.getStatus())
      .appendText(" ('").appendText(item.getStatusInfo().getReasonPhrase()).appendText("')");
    return false;
  }

  public void describeTo(final Description description) {
    description.appendText("HTTP status ").appendValue(expected.getStatusCode())
      .appendText(" ('").appendText(expected.getReasonPhrase()).appendText("')");
  }
}
