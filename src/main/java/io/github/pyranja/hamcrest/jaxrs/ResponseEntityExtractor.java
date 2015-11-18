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

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

final class ResponseEntityExtractor<CONTENT> extends FeatureMatcher<Response, CONTENT> {
  private final Class<CONTENT> type;

  public ResponseEntityExtractor(final Class<CONTENT> type, final Matcher<? super CONTENT> expectation) {
    super(requireNonNull(expectation), "response with body", "body");
    this.type = requireNonNull(type);
  }

  @Override
  protected CONTENT featureValueOf(final Response response) {
    response.bufferEntity();
    return response.readEntity(type);
  }
}
