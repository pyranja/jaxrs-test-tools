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

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResponseMimeExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    new ResponseMimeExtractor(null);
  }

  @Test
  public void shouldExtractMediaTypeFromResponse() throws Exception {
    final ResponseMimeExtractor subject = new ResponseMimeExtractor(any(MediaType.class));
    final Response response = MockResponse.having(MediaType.APPLICATION_XML_TYPE);
    assertThat(subject.featureValueOf(response), equalTo(MediaType.APPLICATION_XML_TYPE));
  }

}
