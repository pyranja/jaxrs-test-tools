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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import io.github.pyranja.hamcrest.Hamcrest;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class ResponseHeadersExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    JaxrsMatchers.headers(null);
  }

  @Test
  public void shouldWorkThroughFactoryMethod() throws Exception {
    final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
    headers.putSingle("test-header", "test-value");
    assertThat(MockResponse.withHeaders(headers),
      JaxrsMatchers.headers(hasEntry(equalTo("test-header"), contains("test-value"))));
  }

  @Test
  public void shortcutShouldAcceptHeadersWithExactMatch() throws Exception {
    final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
    headers.putSingle("test-header", "test-value");
    assertThat(MockResponse.withHeaders(headers),
      JaxrsMatchers.hasHeader("test-header", "test-value"));
  }

  @Test
  public void shortcutShouldAcceptHeadersWithFuzzyMatch() throws Exception {
    final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
    headers.putSingle("test-header", "test-value");
    headers.add("test-header", "other-value");
    assertThat(MockResponse.withHeaders(headers),
      JaxrsMatchers.hasHeader("test-header", "test-value"));
  }

  @Test
  public void shortcutShouldRejectHeadersWhereExpectedValueIsMissing() throws Exception {
    final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
    headers.putSingle("test-header", "other-value");
    assertThat(MockResponse.withHeaders(headers),
      not(JaxrsMatchers.hasHeader("test-header", "test-value")));
  }

  @Test
  public void shortcutShouldRejectHeadersWhereExpectedKeyIsMissing() throws Exception {
    final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
    assertThat(MockResponse.withHeaders(headers),
      not(JaxrsMatchers.hasHeader("test-header", "test-value")));
  }

  @Test
  public void shouldExtractHeadersFromResponse() throws Exception {
    final ResponseHeadersExtractor subject =
      new ResponseHeadersExtractor(Hamcrest.<MultivaluedMap<String, String>>tautology());
    final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
    final Response response = MockResponse.withHeaders(headers);
    assertThat(subject.featureValueOf(response),
      Matchers.<MultivaluedMap<String, String>>sameInstance(headers));
  }
}
