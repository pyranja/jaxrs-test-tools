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

import io.github.pyranja.hamcrest.Hamcrest;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class ResponseStatusExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    JaxrsMatchers.hasStatus(null);
  }

  @Test
  public void shouldWorkThroughFactoryMethod() throws Exception {
    assertThat(MockResponse.with(Response.Status.OK),
      JaxrsMatchers.hasStatus(equalTo(Response.Status.OK)));
  }

  @Test
  public void shouldExtractStatusFromResponse() throws Exception {
    final ResponseStatusExtractor subject =
      new ResponseStatusExtractor(Hamcrest.<Response.StatusType>tautology());
    final Response response = MockResponse.with(Response.Status.NOT_FOUND);
    assertThat(subject.featureValueOf(response),
      Matchers.<Response.StatusType>equalTo(Response.Status.NOT_FOUND));
  }
}
