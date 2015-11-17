package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Matcher;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Factory methods for JAX-RS specific {@link org.hamcrest.Matcher}.
 */
@SuppressWarnings("unused")
public final class JaxrsMatchers {
  private JaxrsMatchers() { /* no instances */ }

  /**
   * Apply the given {@link Matcher expectation} to the status of a {@link Response}.
   * For example:
   * <pre>
   *   assertThat(response, hasStatus(equalTo(Response.Status.NOT_FOUND)))
   * </pre>
   *
   * @param expectation describes the expected response status
   */
  @SuppressWarnings("unchecked")
  public static Matcher<? super Response> hasStatus(final Matcher<? extends Response.StatusType> expectation) {
    // different generic wildcards to ease interoperability with jax-rs status enum
    // casting here is safe, as matched objects are always type checked at runtime
    return new ResponseStatusExtractor((Matcher<? super Response.StatusType>) expectation);
  }

  /**
   * Matches if a {@link javax.ws.rs.core.Response.Status} belongs to the expected family.
   *
   * @param expectation specifies the expected status family
   */
  public static Matcher<? super Response.StatusType> fromFamily(final Response.Status.Family expectation) {
    return new FromFamily(expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to the content type of a {@link Response}.
   * For example:
   * <pre>
   *   assertThat(response, hasMime(equalTo(MediaType.APPLICATION_XML_TYPE)))
   * </pre>
   *
   * @see #compatibleTo(MediaType)
   *
   * @param expectation describes the expected content type
   */
  public static Matcher<? super Response> hasMime(final Matcher<? super MediaType> expectation) {
    return new ResponseMimeExtractor(expectation);
  }

  /**
   * Matches if a {@link MediaType} is {@link MediaType#isCompatible(MediaType) compatible} with
   * the expectation. In addition to jax-rs compatibility, this matcher accepts content types using
   * an extended syntax as defined in <a href="https://www.ietf.org/rfc/rfc3023.txt">RFC 3023</a>.
   * For example a matcher expecting {@code application/xml}, will accept an actual content type
   * like {@code application/xhtml+xml}, as the subtype suffix is matching.
   *
   * @see IsCompatibleMediaType
   * @see #hasMime(Matcher)
   *
   * @param expected specifies the expected media type
   */
  public static Matcher<? super MediaType> compatibleTo(final MediaType expected) {
    return new IsCompatibleMediaType(expected);
  }

  /**
   * Apply the given {@link Matcher expectation} to the entity of a {@link Response}.
   * For example:
   * <pre>
   *    assertThat(response, hasEntity(MyBean.class, equalTo(..)));
   * </pre>
   *
   * Consider using the shortcuts for asserting text and raw responses.
   *
   * @see #hasTextEntity(Matcher)
   * @see #hasRawEntity(Matcher)
   * @see Response#readEntity(Class)
   *
   * @param type expected type of response entity
   * @param expectation describes the expected response entity
   * @param <CONTENT> expected type of response entity
   */
  public static <CONTENT> Matcher<? super Response> hasEntity(final Class<CONTENT> type,
                                                              final Matcher<? super CONTENT> expectation) {
    return new ResponseEntityExtractor<>(type, expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to the text of a response entity.
   * For example:
   * <pre>
   *   assertThat(response, hasTextEntity(equalTo("my response text")));
   * </pre>
   *
   * @see #hasEntity(Class, Matcher)
   *
   * @param expectation describes the expected text
   */
  public static Matcher<? super Response> hasTextEntity(final Matcher<? super String> expectation) {
    return new ResponseEntityExtractor<>(String.class, expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to a raw response entity.
   * For example:
   * <pre>
   *   assertThat(response, hasRawEntity(equalTo(new byte[] { .. })));
   * </pre>
   *
   * @see #hasEntity(Class, Matcher)
   *
   * @param expectation describes the expected binary entity
   */
  public static Matcher<? super Response> hasRawEntity(final Matcher<? super byte[]> expectation) {
    return new ResponseEntityExtractor<>(byte[].class, expectation);
  }
}
