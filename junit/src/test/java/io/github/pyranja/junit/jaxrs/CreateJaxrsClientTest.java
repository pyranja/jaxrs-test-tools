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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class CreateJaxrsClientTest {

  private static final URI TEST_URI = URI.create("http://localhost:8080/test");

  @Test
  public void shouldBeATestRule() throws Exception {
    assertThat(CreateJaxrsClient.forEndpoint(TEST_URI).usingDefaults(), instanceOf(TestRule.class));
  }

  @Test(expected = NullPointerException.class)
  public void shouldRejectNullClient() throws Exception {
    new CreateJaxrsClient(null, TEST_URI);
  }

  @Test(expected = NullPointerException.class)
  public void shouldRejectNullBaseUri() throws Exception {
    new CreateJaxrsClient(ClientBuilder.newClient(), null);
  }

  @Test
  public void shouldProvideClientInstance() throws Exception {
    final JerseyClient expected = JerseyClientBuilder.createClient();
    final CreateJaxrsClient subject = CreateJaxrsClient.forEndpoint(TEST_URI).using(expected);
    assertThat(subject.client(), Matchers.<Client>sameInstance(expected));
  }

  @Test
  public void shouldUseProvidedBaseUriAsDefaultTarget() throws Exception {
    final CreateJaxrsClient subject = CreateJaxrsClient.forEndpoint(TEST_URI).usingDefaults();
    assertThat(subject.target().getUri(), equalTo(TEST_URI));
  }

  @Test
  public void shouldAppendProvidedPathSegments() throws Exception {
    final URI base = URI.create("http://localhost:8080/test");
    final CreateJaxrsClient subject = CreateJaxrsClient.forEndpoint(base).usingDefaults();
    final URI resolved = URI.create("http://localhost:8080/test/first/second");
    final WebTarget target = subject.target("first", "second");
    assertThat(target.getUri(), equalTo(resolved));
  }

  @Test
  public void shouldCloseClientAfterTest() throws Exception {
    final JerseyClient client = JerseyClientBuilder.createClient();
    CreateJaxrsClient.forEndpoint(TEST_URI).using(client).after();
    assertThat("client not closed after tear down", client.isClosed(), equalTo(true));
  }
}
