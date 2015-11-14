package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Matcher;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Factory methods for all included JAX-RS specific {@link org.hamcrest.Matcher}.
 */
@SuppressWarnings("unused")
public final class JaxrsMatchers {
  private JaxrsMatchers() { /* no instances */ }

  /**
   * Matches if the examined {@link Response} has the given http status.
   *
   * @param expected the expected response status
   */
  public static Matcher<Response> hasStatus(final Response.Status expected) {
    return new ResponseStatusMatcher(expected);
  }

  /**
   * Matches if the examined {@link Response} has the given http status.
   *
   * @param expected the expected response status code.
   */
  public static Matcher<Response> hasStatus(final int expected) {
    return new ResponseStatusMatcher(Response.Status.fromStatusCode(expected));
  }

  /**
   * Matches if the examined {@link Response} has a status belonging to the given response family.
   *
   * @param expected family of expected http status
   */
  public static Matcher<Response> hasFamily(final Response.Status.Family expected) {
    return new ResponseStatusFamilyMatcher(expected);
  }

  /**
   * Apply the given {@link Matcher expecation} to the content type of a {@link Response}.
   * For example:
   * <pre>assertThat(response, hasMime(equalTo(MediaType.APPLICATION_XML_TYPE)))</pre>
   *
   * @param expectation describes the expected content type
   * @see #compatibleTo(MediaType)
   */
  public static Matcher<Response> hasMime(final Matcher<? super MediaType> expectation) {
    return new ResponseMimeExtractor(expectation);
  }

  /**
   * Matches if a {@link MediaType} is {@link MediaType#isCompatible(MediaType) compatible} with
   * the expectation. In addition to jax-rs compatibility, this matcher accepts content types using
   * an extended syntax as defined in <a href="https://www.ietf.org/rfc/rfc3023.txt">RFC 3023</a>.
   * For example a matcher expecting {@code application/xml}, will accept an actual content type
   * like {@code application/xhtml+xml}, as the subtype suffix is matching.
   *
   * @param expected specifies the expected media type
*    @see IsCompatibleMediaType
   * @see #hasMime(Matcher)
   */
  public static Matcher<MediaType> compatibleTo(final MediaType expected) {
    return new IsCompatibleMediaType(expected);
  }
}
