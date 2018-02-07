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

import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Factory methods for jaxrs.Response mocks
 */
public final class MockResponse {
  private MockResponse() { /* no instances */ }

  /**
   * Create mock response with given status only.
   */
  static Response with(final Response.StatusType status) {
    final Response mock = Mockito.mock(Response.class);
    when(mock.getStatusInfo()).thenReturn(status);
    when(mock.getStatus()).thenReturn(status.getStatusCode());
    return mock;
  }

  /**
   * Create mock response with given content type.
   */
  static Response having(final MediaType mime) {
    final Response mock = Mockito.mock(Response.class);
    when(mock.getMediaType()).thenReturn(mime);
    return mock;
  }

  /**
   * Create mock response with given entity.
   */
  static <CONTENT> Response containing(final Class<CONTENT> type, final CONTENT content) {
    final Response mock = Mockito.mock(Response.class);
    when(mock.hasEntity()).thenReturn(true);
    when(mock.getEntity()).thenReturn(content);
    when(mock.readEntity(type)).thenReturn(content);
    return mock;
  }

  /**
   * Create mock response with given header map.
   */
  public static Response withHeaders(final MultivaluedMap<String, String> headers) {
    final Response mock = Mockito.mock(Response.class);
    final MultivaluedHashMap<String, Object> untyped =
      new MultivaluedHashMap<String, Object>(headers);
    when(mock.getHeaders()).thenReturn(untyped);
    when(mock.getStringHeaders()).thenReturn(headers);
    return mock;
  }
}
