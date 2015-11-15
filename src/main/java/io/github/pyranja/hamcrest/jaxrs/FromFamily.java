package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

final class FromFamily extends TypeSafeDiagnosingMatcher<Response.StatusType> {
  private final Response.Status.Family expected;

  public FromFamily(final Response.Status.Family expected) {
    this.expected = requireNonNull(expected);
  }

  @Override
  protected boolean matchesSafely(final Response.StatusType item, final Description mismatch) {
    mismatch.appendText("was ").appendValue(item.getStatusCode())
      .appendText(" ('").appendText(item.getReasonPhrase()).appendText("') from ")
      .appendValue(item.getFamily());
    return expected.equals(item.getFamily());
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("from family ").appendValue(expected);
  }
}
