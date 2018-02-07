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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;

import org.hamcrest.Matcher;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
   *   assertThat(response, status(equalTo(Response.Status.NOT_FOUND)))
   * </pre>
   *
   * @param expectation describes the expected response status
   * @return the matcher instance
   */
  @SuppressWarnings("unchecked")
  public static Matcher<? super Response> status(final Matcher<? extends Response.StatusType> expectation) {
    // different generic wildcards to ease interoperability with jax-rs status enum
    // casting here is safe, as matched objects are always type checked at runtime
    return new ResponseStatusExtractor((Matcher<? super Response.StatusType>) expectation);
  }

  /**
   * Matches if a {@link javax.ws.rs.core.Response.Status} belongs to the expected family.
   *
   * @param expectation specifies the expected status family
   * @return the matcher instance
   */
  public static Matcher<? super Response.StatusType> fromFamily(final Response.Status.Family expectation) {
    return new FromFamily(expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to the content type of a {@link Response}.
   * For example:
   * <pre>
   *   assertThat(response, mime(equalTo(MediaType.APPLICATION_XML_TYPE)))
   * </pre>
   *
   * @see #compatibleTo(MediaType)
   *
   * @param expectation describes the expected content type
   * @return the matcher instance
   */
  public static Matcher<? super Response> mime(final Matcher<? super MediaType> expectation) {
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
   * @see #mime(Matcher)
   *
   * @param expected specifies the expected media type
   * @return the matcher instance
   */
  public static Matcher<? super MediaType> compatibleTo(final MediaType expected) {
    return new IsCompatibleMediaType(expected);
  }

  /**
   * Apply the given {@link Matcher expectation} to the entity of a {@link Response}.
   * For example:
   * <pre>
   *    assertThat(response, entity(MyBean.class, equalTo(..)));
   * </pre>
   *
   * <p>
   *   <strong>Note:</strong> Consider using the shortcuts for asserting text and raw responses.
   * </p>
   *
   * @see #textEntity(Matcher)
   * @see #rawEntity(Matcher)
   * @see Response#readEntity(Class)
   *
   * @param type expected type of response entity
   * @param expectation describes the expected response entity
   * @param <CONTENT> expected type of response entity
   * @return the matcher instance
   */
  public static <CONTENT> Matcher<? super Response> entity(final Class<CONTENT> type,
                                                           final Matcher<? super CONTENT> expectation) {
    return new ResponseEntityExtractor<>(type, expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to the text of a response entity.
   * For example:
   * <pre>
   *   assertThat(response, textEntity(equalTo("my response text")));
   * </pre>
   *
   * @see #entity(Class, Matcher)
   *
   * @param expectation describes the expected text
   * @return the matcher instance
   */
  public static Matcher<? super Response> textEntity(final Matcher<? super String> expectation) {
    return new ResponseEntityExtractor<>(String.class, expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to a raw response entity.
   * For example:
   * <pre>
   *   assertThat(response, rawEntity(equalTo(new byte[] { .. })));
   * </pre>
   *
   * @see #entity(Class, Matcher)
   *
   * @param expectation describes the expected binary entity
   * @return the matcher instance
   */
  public static Matcher<? super Response> rawEntity(final Matcher<? super byte[]> expectation) {
    return new ResponseEntityExtractor<>(byte[].class, expectation);
  }

  /**
   * Apply the given {@link Matcher expectation} to the header map of a {@link Response}.
   * For example:
   * <pre>
   *   assertThat(response, headers(hasEntry(equalTo("Connection"), contains("close"))));
   * </pre>
   *
   * <p>
   *   <strong>Note:</strong> Consider using the shortcut method to check for a single header value.
   * </p>
   *
   * @see #hasHeader(String, String)
   *
   * @param expectation describes the expected header map
   * @return the matcher instance
   */
  public static Matcher<? super Response> headers(final Matcher<? super MultivaluedMap<String, String>> expectation) {
    return new ResponseHeadersExtractor(expectation);
  }

  /**
   * Matches if a {@link Response} has a header with the expected {@code name} and {@code value}.
   * If multiple headers with the expected name are present, at least one must have the expected
   * value. The order of headers is ignored.
   *
   * @param name name of header
   * @param value expected value
   * @return the matcher instance
   */
  public static Matcher<? super Response> hasHeader(final String name, final String value) {
    return new ResponseHeadersExtractor(hasEntry(equalTo(name), hasItem(value)));
  }
}
