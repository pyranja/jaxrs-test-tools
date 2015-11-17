package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

final class ResponseEntityExtractor<CONTENT> extends FeatureMatcher<Response, CONTENT> {
  private final Class<CONTENT> type;

  public ResponseEntityExtractor(final Class<CONTENT> type, final Matcher<? super CONTENT> expectation) {
    super(requireNonNull(expectation), "response with body", "body");
    this.type = requireNonNull(type);
  }

  @Override
  protected CONTENT featureValueOf(final Response response) {
    response.bufferEntity();
    return response.readEntity(type);
  }
}
