package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

/**
 * Factory methods for all included JAX-RS specific {@link org.hamcrest.Matcher}.
 */
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
}
