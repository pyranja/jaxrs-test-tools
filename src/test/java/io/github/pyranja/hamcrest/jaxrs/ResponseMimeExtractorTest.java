package io.github.pyranja.hamcrest.jaxrs;

import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ResponseMimeExtractorTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    new ResponseMimeExtractor(null);
  }

  @Test
  public void shouldExtractMediaTypeFromResponse() throws Exception {
    final ResponseMimeExtractor subject = new ResponseMimeExtractor(any(MediaType.class));
    final Response response = MockResponse.having(MediaType.APPLICATION_XML_TYPE);
    assertThat(subject.featureValueOf(response), equalTo(MediaType.APPLICATION_XML_TYPE));
  }

}
