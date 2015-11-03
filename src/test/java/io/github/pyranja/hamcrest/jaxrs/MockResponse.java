package io.github.pyranja.hamcrest.jaxrs;

import org.mockito.Mockito;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.when;

/**
 * Factory methods for jaxrs.Response mocks
 */
public final class MockResponse {
  private MockResponse() { /* no instances */ }

  /**
   * Create mock response with given status only.
   */
  static Response with(final Response.Status status) {
    final Response mock = Mockito.mock(Response.class);
    when(mock.getStatusInfo()).thenReturn(status);
    when(mock.getStatus()).thenReturn(status.getStatusCode());
    return mock;
  }
}
