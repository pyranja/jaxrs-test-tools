package io.github.pyranja.hamcrest;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static io.github.pyranja.hamcrest.Hamcrest.isFalse;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Define and test expected behavior of {@link FeatureMatcher}, as required by jaxrs matcher
 * implementations.
 */
public class FeatureMatcherTest {

  @Test
  public void shouldRejectIfExpectationNotMet() throws Exception {
    assertThat(Integer.MIN_VALUE, not(matching(Hamcrest.contradiction())));
  }

  @Test
  public void shouldMatchIfExpectationIsMet() throws Exception {
    assertThat(Integer.MIN_VALUE, matching(Hamcrest.tautology()));
  }

  @Test
  public void shouldRejectNullInput() throws Exception {
    assertThat(null, not(matching(Hamcrest.tautology())));
  }

  @Test
  public void shouldRejectInputOnTypeMismatch() throws Exception {
    assertThat(matching(Hamcrest.tautology()).matches(Long.MAX_VALUE), isFalse());
  }

  private FakeIntegerFeatureMatcher matching(final Matcher<Object> contradiction) {
    return new FakeIntegerFeatureMatcher(contradiction);
  }

  private static class FakeIntegerFeatureMatcher extends FeatureMatcher<Integer, Integer> {
    public FakeIntegerFeatureMatcher(final Matcher<? super Integer> subMatcher) {
      super(subMatcher, "test feature", "test");
    }

    @Override
    protected Integer featureValueOf(final Integer actual) {
      return Integer.MIN_VALUE;
    }
  }
}
