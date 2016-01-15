package io.github.pyranja.jaxrs;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class JaxrsWatcherTest {

  private final JaxrsWatcher sut = new JaxrsWatcher();
  private final ClientRequestContext request = Mockito.mock(ClientRequestContext.class);

  @Test
  public void shouldObserveAnHttpExchangePerRequest() throws Exception {
    for (int i = 0; i < 11; i++) {
      assertThat(sut.observations(), hasSize(i));
      sut.filter(request);
    }
  }

  @Test(expected = IllegalStateException.class)
  public void singleShouldFailIfNoObservationsFound() throws Exception {
    sut.single();
  }

  @Test(expected = IllegalStateException.class)
  public void singleShouldFailIfMultipleObservationsFound() throws Exception {
    sut.filter(request);
    sut.filter(request);
    sut.single();
  }

  @Test
  public void shouldRecordMethodAndUriOfObservedRequest() throws Exception {
    when(request.getMethod()).thenReturn("my http method");
    when(request.getUri()).thenReturn(URI.create("urn:test:my_uri"));
    sut.filter(request);
    assertThat(sut.single().request().getMethod(), equalTo("my http method"));
    assertThat(sut.single().request().getUri(), equalTo(URI.create("urn:test:my_uri")));
  }

  @Test
  public void shouldRecordHeadersOfObservedRequest() throws Exception {
    final MultivaluedHashMap<String, String> expected = new MultivaluedHashMap<>();
    when(request.getStringHeaders()).thenReturn(expected);
    sut.filter(request);
    assertThat(sut.single().request().getHeaders(), Matchers.<MultivaluedMap<String, String>>sameInstance(expected));
  }
}
