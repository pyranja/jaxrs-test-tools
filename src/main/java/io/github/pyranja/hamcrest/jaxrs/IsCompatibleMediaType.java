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
