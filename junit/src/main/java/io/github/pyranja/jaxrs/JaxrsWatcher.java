package io.github.pyranja.jaxrs;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Observe http exchanges on a JAX-RS component.
 */
@Provider
public final class JaxrsWatcher implements ClientRequestFilter {

  private final List<ObservedHttpExchange> exchanges = new CopyOnWriteArrayList<>();

  @Override
  public void filter(final ClientRequestContext context) throws IOException {
    final ObservedHttpExchange.Request request = ObservedHttpExchange
      .captureRequest(context.getMethod(), context.getUri(), context.getStringHeaders());
    exchanges.add(ObservedHttpExchange.create().withRequest(request));
  }

  public List<ObservedHttpExchange> observations() {
    return Collections.unmodifiableList(exchanges);
  }

  public ObservedHttpExchange single() {
    if (exchanges.size() != 1) {
      throw new IllegalStateException("expected a single exchange but observed " + exchanges);
    }
    return exchanges.get(0);
  }
}
