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
package io.github.pyranja.hamcrest.jaxrs;

import static java.util.Objects.requireNonNull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.ws.rs.core.Response;

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
