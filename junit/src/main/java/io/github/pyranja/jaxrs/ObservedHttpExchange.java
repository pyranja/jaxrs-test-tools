package io.github.pyranja.jaxrs;

import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

public final class ObservedHttpExchange {
  public static ObservedHttpExchange create() {
    return new ObservedHttpExchange();
  }

  public static Request captureRequest(final String method, final URI uri, final MultivaluedMap<String, String> headers) {
    return new Request(method, uri, headers);
  }

  // request and response are updated during observation
  private volatile Request request;

  private ObservedHttpExchange() {
  }

  public Request request() {
    return request;
  }

  public ObservedHttpExchange withRequest(final Request request) {
    this.request = request;
    return this;
  }

  public static final class Request {
    private final String method;
    private final URI uri;
    private final MultivaluedMap<String, String> headers;

    private Request(final String method, final URI uri, final MultivaluedMap<String, String> headers) {
      this.method = method;
      this.uri = uri;
      this.headers = headers;
    }

    public String getMethod() {
      return method;
    }

    public URI getUri() {
      return uri;
    }

    public MultivaluedMap<String, String> getHeaders() {
      return headers;
    }
  }
}
