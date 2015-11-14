package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

final class ResponseMimeExtractor extends FeatureMatcher<Response, MediaType> {
  public ResponseMimeExtractor(final Matcher<? super MediaType> expectation) {
    super(requireNonNull(expectation), "response with content type", "content type");
  }

  @Override
  protected MediaType featureValueOf(final Response actual) {
    return actual.getMediaType();
  }
}
