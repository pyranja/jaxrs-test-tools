package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.ws.rs.core.Response;
import java.util.Objects;

final class ResponseStatusFamilyMatcher extends TypeSafeDiagnosingMatcher<Response> {
  private final Response.Status.Family expected;

  public ResponseStatusFamilyMatcher(final Response.Status.Family expected) {
    this.expected = Objects.requireNonNull(expected);
  }

  @Override
  protected boolean matchesSafely(final Response item, final Description mismatchDescription) {
    if (item.getStatusInfo().getFamily() == expected) {
      return true;
    }
    mismatchDescription.appendValue(item.getStatus())
      .appendText(" ('").appendText(item.getStatusInfo().getReasonPhrase()).appendText("')");
    return false;
  }

  public void describeTo(final Description description) {
    description.appendText("HTTP ").appendValue(expected);
  }
}
