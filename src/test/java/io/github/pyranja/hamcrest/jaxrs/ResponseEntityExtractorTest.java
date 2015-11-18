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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import io.github.pyranja.hamcrest.Hamcrest;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.nio.charset.Charset;

public class ResponseEntityExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    JaxrsMatchers.hasEntity(Void.class, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectedType() throws Exception {
    JaxrsMatchers.hasEntity(null, Hamcrest.tautology());
  }

  @Test
  public void shouldExtractEntityOfResponse() throws Exception {
    final ResponseEntityExtractor<String> subject =
      new ResponseEntityExtractor<>(String.class, Hamcrest.<String>tautology());
    final Response response = MockResponse.containing(String.class, "test");
    assertThat(subject.featureValueOf(response), equalTo("test"));
  }

  @Test
  public void shouldBufferEntityBeforeExtraction() throws Exception {
    final ResponseEntityExtractor<String> subject =
      new ResponseEntityExtractor<>(String.class, Hamcrest.<String>tautology());
    final Response response = MockResponse.containing(String.class, "test");
    subject.featureValueOf(response);
    verify(response).bufferEntity();
  }

  @Test
  public void shortcutShouldExtractTextEntity() throws Exception {
    final Response response = MockResponse.containing(String.class, "test");
    assertThat(response, JaxrsMatchers.hasTextEntity(equalTo("test")));
  }

  @Test
  public void shortcutShouldExtractRawEntity() throws Exception {
    final byte[] payload = "test".getBytes(Charset.forName("UTF-8"));
    final Response response = MockResponse.containing(byte[].class, payload);
    assertThat(response, JaxrsMatchers.hasRawEntity(equalTo(payload)));
  }
}
