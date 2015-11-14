package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.core.MediaType;

import static java.util.Objects.requireNonNull;

/**
 * Matches if a given media type is compatible with the expectation as defined by
 * {@link MediaType#isCompatible(MediaType)}.
 */
public final class IsCompatibleMediaType extends TypeSafeMatcher<MediaType> {
  private final MediaType expected;

  public IsCompatibleMediaType(final MediaType expected) {
    this.expected = requireNonNull(expected);
  }

  @Override
  protected boolean matchesSafely(final MediaType item) {
    return expected.isCompatible(item);
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("compatible to ").appendValue(expected);
  }
}
