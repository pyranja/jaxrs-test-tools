package io.github.pyranja.hamcrest.jaxrs;

import io.github.pyranja.hamcrest.Hamcrest;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertThat;

public class ResponseStatusExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    new ResponseStatusExtractor(null);
  }

  @Test
  public void shouldExtractStatusFromResponse() throws Exception {
    final ResponseStatusExtractor subject =
      new ResponseStatusExtractor(Hamcrest.<Response.StatusType>tautology());
    final Response response = MockResponse.with(Response.Status.NOT_FOUND);
    assertThat(subject.featureValueOf(response),
      Matchers.<Response.StatusType>equalTo(Response.Status.NOT_FOUND));
  }
}
