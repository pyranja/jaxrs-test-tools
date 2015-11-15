package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.core.MediaType;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

final class IsCompatibleMediaType extends TypeSafeMatcher<MediaType> {
  private final MediaType expected;

  public IsCompatibleMediaType(final MediaType expected) {
    this.expected = requireNonNull(expected);
  }

  @Override
  protected boolean matchesSafely(final MediaType item) {
    return expected.isCompatible(item) || suffixMatching(item);
  }

  private boolean suffixMatching(final MediaType item) {
    // extended naming convention as described in RFC3023: type/*+subtype
    final String suffixRegex = String.format(Locale.ENGLISH, ".+\\+%s", expected.getSubtype());
    return item.getType().equalsIgnoreCase(expected.getType())
      && item.getSubtype().matches(suffixRegex);
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("compatible to ").appendValue(expected);
  }
}
