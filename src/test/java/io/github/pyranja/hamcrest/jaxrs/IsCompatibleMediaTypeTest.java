package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class IsCompatibleMediaTypeTest {

  @DataPoints
  public static final MediaType[] mediaTypes = new MediaType[] {
    MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_FORM_URLENCODED_TYPE,
    MediaType.TEXT_HTML_TYPE, MediaType.TEXT_PLAIN_TYPE, MediaType.TEXT_XML_TYPE,
    MediaType.MULTIPART_FORM_DATA_TYPE,
    new MediaType("image", "jpeg")
  };

  @Test(expected = NullPointerException.class)
  public void shouldFailFastOnNullExpectation() throws Exception {
    subjectExpecting(null);
  }

  @Theory
  public void shouldMatchOnEqualMediaType(final MediaType mime) throws Exception {
    final IsCompatibleMediaType subject = subjectExpecting(mime);
    assertThat(subject.matches(mime), equalTo(true));
  }

  @Theory
  public void shouldMatchIfTypeMatchesAndSubtypeIsWildcard(final MediaType mime) throws Exception {
    final IsCompatibleMediaType subject = subjectExpecting(new MediaType(mime.getType(), "*"));
    assertThat(subject.matches(mime), equalTo(true));
  }

  @Theory
  public void shouldMatchIfExpectingWildcard(final MediaType mime) throws Exception {
    final IsCompatibleMediaType subject = subjectExpecting(MediaType.WILDCARD_TYPE);
    assertThat(subject.matches(mime), equalTo(true));
  }

  @Theory
  public void shouldRejectIfTypeNotMatching(final MediaType mime) throws Exception {
    final IsCompatibleMediaType subject =
      subjectExpecting(new MediaType("illegal", mime.getSubtype()));
    assertThat(subject.matches(mime), equalTo(false));
  }

  @Theory
  public void shouldRejectIfTotalMisMatch(final MediaType mime) throws Exception {
    final IsCompatibleMediaType subject =
      subjectExpecting(new MediaType("illegal", "type"));
    assertThat(subject.matches(mime), equalTo(false));
  }

  @Theory
  public void shouldIncludeExpectedTypeInDescription(final MediaType expected) throws Exception {
    final IsCompatibleMediaType subject = subjectExpecting(expected);
    final StringDescription description = new StringDescription();
    subject.describeTo(description);
    assertThat(description.toString(), containsString(expected.toString()));
  }

  @Theory
  public void shouldIncludeActualMimeStringInMismatchDescription(final MediaType actual) throws Exception {
    final IsCompatibleMediaType subject = subjectExpecting(new MediaType("illegal", "type"));
    final StringDescription description = new StringDescription();
    subject.describeMismatch(actual, description);
    assertThat(description.toString(), containsString(actual.toString()));
  }

  private IsCompatibleMediaType subjectExpecting(final MediaType expected) {
    return new IsCompatibleMediaType(expected);
  }
}
