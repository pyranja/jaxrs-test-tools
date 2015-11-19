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

import static java.util.Objects.requireNonNull;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

final class ResponseMimeExtractor extends FeatureMatcher<Response, MediaType> {
  public ResponseMimeExtractor(final Matcher<? super MediaType> expectation) {
    super(requireNonNull(expectation), "response with content type", "content type");
  }

  @Override
  protected MediaType featureValueOf(final Response actual) {
    return actual.getMediaType();
  }
}
