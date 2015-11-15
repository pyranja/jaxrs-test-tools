package io.github.pyranja.hamcrest.jaxrs;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;

import java.util.Objects;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(Theories.class)
public class FromFamilyTest {

  @Test(expected = NullPointerException.class)
  public void shouldFailOnNullExpectation() throws Exception {
    new FromFamily(null);
  }

  @Theory
  public void shouldMatchIfStatusBelongsToExpectedFamily(final Response.Status status, final Response.Status.Family family) throws Exception {
    assumeThat(status.getFamily(), equalTo(family));
    assertThat(status, new FromFamily(family));
  }

  @Theory
  public void shouldRejectIfStatusDoesNotBelongToExpectedFamily(final Response.Status status, final Response.Status.Family family) throws Exception {
    assumeThat(status.getFamily(), not(equalTo(family)));
    assertThat(status, not(new FromFamily(family)));
  }

  @Theory
  public void shouldIncludeExpectedFamilyInDescription(final Response.Status.Family family) throws Exception {
    final StringDescription description = new StringDescription();
    new FromFamily(family).describeTo(description);
    assertThat(description.toString(), containsString(family.toString()));
  }

  @Theory
  public void shouldIncludeActualStatusCodeAndFamilyInMismatchDescription(final Response.Status status) throws Exception {
    final StringDescription description = new StringDescription();
    new FromFamily(Response.Status.Family.OTHER).describeMismatch(status, description);
    assertThat(description.toString(), containsString(Objects.toString(status.getStatusCode())));
    assertThat(description.toString(), containsString(status.getFamily().toString()));
  }
}
