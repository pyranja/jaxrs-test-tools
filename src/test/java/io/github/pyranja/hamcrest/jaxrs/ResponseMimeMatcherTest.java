package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ResponseMimeMatcherTest {

  private static final CustomMatcher<MediaType> CONTRADICTION =
    new CustomMatcher<MediaType>("contradiction") {
      @Override
      public boolean matches(final Object ignored) {
        return false;
      }
    };
  private static final CustomMatcher<MediaType> TAUTOLOGY = new CustomMatcher<MediaType>("tautology") {
    @Override
    public boolean matches(final Object ignored) {
      return true;
    }
  };

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullReference() throws Exception {
    subjectExpecting(null);
  }

  @Test
  public void shouldRejectIfExpectationNotMet() throws Exception {
    assertThat(subjectExpecting(CONTRADICTION).matches(MockResponse.having(MediaType.APPLICATION_JSON_TYPE)), equalTo(false));
  }

  @Test
  public void shouldMatchIfExpectationIsMet() throws Exception {
    assertThat(subjectExpecting(TAUTOLOGY).matches(MockResponse.having(MediaType.APPLICATION_JSON_TYPE)), equalTo(true));
  }

  @Test
  public void shouldExtractMediaTypeFromResponse() throws Exception {
    final ResponseMimeMatcher subject = subjectExpecting(any(MediaType.class));
    final Response response = MockResponse.having(MediaType.APPLICATION_XML_TYPE);
    assertThat(subject.featureValueOf(response), equalTo(MediaType.APPLICATION_XML_TYPE));
  }

  private ResponseMimeMatcher subjectExpecting(final Matcher<MediaType> expectation) {
    return new ResponseMimeMatcher(expectation);
  }
}
