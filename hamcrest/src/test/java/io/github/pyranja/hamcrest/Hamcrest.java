/*
 * Copyright © 2015 Chris Borckholder (chris.borckholder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.pyranja.hamcrest;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

public final class Hamcrest {
  private Hamcrest() { /* no instances */ }

  /**
   * Expect {@link Boolean#TRUE}
   */
  public static Matcher<Boolean> isTrue() {
    return new IsEqual<>(Boolean.TRUE);
  }

  /**
   * Expect {@link Boolean#FALSE}
   */
  public static Matcher<Boolean> isFalse() {
    return new IsEqual<>(Boolean.FALSE);
  }

  /**
   * Create a {@link Matcher} that always matches.
   */
  public static <T> Matcher<T> tautology() {
    return new Tautology<>();
  }

  /**
   * Create a {@link Matcher} that never matches.
   */
  public static <T> Matcher<T> contradiction() {
    return new Contradiction<>();
  }

  private static final class Tautology<T> extends CustomMatcher<T> {
    public Tautology() {
      super("tautology");
    }

    @Override
    public boolean matches(final Object item) {
      return true;
    }
  }

  private static final class Contradiction<T> extends CustomMatcher<T> {
    public Contradiction() {
      super("contradiction");
    }

    @Override
    public boolean matches(final Object item) {
      return false;
    }
  }
}
