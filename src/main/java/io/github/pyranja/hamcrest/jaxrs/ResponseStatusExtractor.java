package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

final class ResponseStatusExtractor extends FeatureMatcher<Response, Response.StatusType> {
  public ResponseStatusExtractor(final Matcher<? super Response.StatusType> expectation) {
    super(requireNonNull(expectation), "response with status", "status");
  }

  @Override
  protected Response.StatusType featureValueOf(final Response actual) {
    return actual.getStatusInfo();
  }
}
