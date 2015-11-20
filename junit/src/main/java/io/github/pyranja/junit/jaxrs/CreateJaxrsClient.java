/*
 * Copyright (C) 2015 Chris Borckholder (chris.borckholder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pyranja.junit.jaxrs;

import static java.util.Objects.requireNonNull;

import org.junit.rules.ExternalResource;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * A {@link org.junit.rules.TestRule JUnit rule}, which manages a {@link javax.ws.rs.client.Client
 * JAX-RS client} instance. WebTargets {@link #target(String...) provided} by the rule will point to
 * the given base URI by default.
 *
 * @see javax.ws.rs.client.Client
 * @see org.junit.rules.TestRule
 */
public final class CreateJaxrsClient extends ExternalResource {

  /**
   * Create the rule with the given base endpoint URI.
   *
   * @param base default target
   * @return fluent builder
   */
  public static Builder forEndpoint(final URI base) {
    return new Builder(base);
  }

  private final Client client;
  private final URI base;

  CreateJaxrsClient(final Client client, final URI base) {
    this.client = requireNonNull(client);
    this.base = requireNonNull(base);
  }

  /**
   * Direct access to the managed client instance.
   *
   * @return the managed client instance
   */
  public Client client() {
    return client;
  }

  /**
   * Create a {@link WebTarget} using the set base URI. Additional path segments may be provided
   * and are added in order to the target URI. Segments may contain {@code '/'} separators.
   *
   * @see Client#target(URI)
   * @see WebTarget#path(String)
   *
   * @param segments optional additional path segments
   * @return WebTarget created through the managed client
   */
  public WebTarget target(final String... segments) {
    WebTarget target = client.target(base);
    for (final String segment : segments) {
      target = target.path(segment);
    }
    return target;
  }

  @Override
  protected void after() {
    client.close();
  }

  public static final class Builder {
    private final URI base;

    private Builder(final URI base) {
      this.base = base;
    }

    /**
     * Uses the given, pre-configured client.
     *
     * @param client custom JAX-RS client
     * @return initialized rule
     */
    public CreateJaxrsClient using(final Client client) {
      return new CreateJaxrsClient(client, this.base);
    }

    /**
     * Uses a client with {@link javax.ws.rs.client.ClientBuilder#newClient() default}
     * configuration.
     *
     * @return initialized rule
     */
    public CreateJaxrsClient usingDefaults() {
      return using(ClientBuilder.newClient());
    }
  }
}
