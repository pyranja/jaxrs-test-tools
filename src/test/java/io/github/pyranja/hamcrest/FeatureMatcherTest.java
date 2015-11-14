package io.github.pyranja.hamcrest;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Define and test expected behavior of {@link FeatureMatcher}, as required by jaxrs matcher
 * implementations.
 */
public class FeatureMatcherTest {

  @Test
  public void shouldRejectIfExpectationNotMet() throws Exception {
    final FeatureMatcher<?, ?> subject = new FakeIntegerFeatureMatcher(Hamcrest.contradiction());
    assertThat(subject.matches(new Object()), equalTo(false));
  }

  @Test
  public void shouldMatchIfExpectationIsMet() throws Exception {
    final FeatureMatcher<?, ?> subject = new FakeIntegerFeatureMatcher(Hamcrest.tautology());
    assertThat(subject.matches(new Object()), equalTo(true));
  }

  @Test
  public void shouldRejectNullInput() throws Exception {
    final FeatureMatcher<?, ?> subject = new FakeIntegerFeatureMatcher(Hamcrest.tautology());
    assertThat(subject.matches(null), equalTo(false));
  }

  @Test
  public void shouldRejectInputOnTypeMismatch() throws Exception {
    // auto type detection only works on 'real' sub-classes, where the method signature of
    // featureValueOf has a concrete return type, not a generic one
    final FeatureMatcher<Integer, Integer> subject =
      new FeatureMatcher<Integer, Integer>(Hamcrest.<Integer>tautology(), "test", "test") {
        @Override
        protected Integer featureValueOf(final Integer actual) {
          return Integer.MIN_VALUE;
        }
      };
    assertThat(subject.matches(Long.MAX_VALUE), equalTo(false));
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
