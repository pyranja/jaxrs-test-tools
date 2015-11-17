package io.github.pyranja.hamcrest.jaxrs;

import io.github.pyranja.hamcrest.Hamcrest;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class ResponseEntityExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    JaxrsMatchers.hasEntity(Void.class, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectedType() throws Exception {
    JaxrsMatchers.hasEntity(null, Hamcrest.tautology());
  }

  @Test
  public void shouldExtractEntityOfResponse() throws Exception {
    final ResponseEntityExtractor<String> subject =
      new ResponseEntityExtractor<>(String.class, Hamcrest.<String>tautology());
    final Response response = MockResponse.containing(String.class, "test");
    assertThat(subject.featureValueOf(response), equalTo("test"));
  }

  @Test
  public void shouldBufferEntityBeforeExtraction() throws Exception {
    final ResponseEntityExtractor<String> subject =
      new ResponseEntityExtractor<>(String.class, Hamcrest.<String>tautology());
    final Response response = MockResponse.containing(String.class, "test");
    subject.featureValueOf(response);
    verify(response).bufferEntity();
  }

  @Test
  public void shortcutShouldExtractTextEntity() throws Exception {
    final Response response = MockResponse.containing(String.class, "test");
    assertThat(response, JaxrsMatchers.hasTextEntity(equalTo("test")));
  }

  @Test
  public void shortcutShouldExtractRawEntity() throws Exception {
    final byte[] payload = "test".getBytes(Charset.forName("UTF-8"));
    final Response response = MockResponse.containing(byte[].class, payload);
    assertThat(response, JaxrsMatchers.hasRawEntity(equalTo(payload)));
  }
}
