package com.scottlogic.deg.generator;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class AssertingMatcher<T> extends DiagnosingMatcher<T> {
    public static <U> Matcher<U> matchesAssertions(
        String description,
        BiConsumer<U, SubAsserter> asserter) {
        return new AssertingMatcher<>(description, asserter);
    }

    private final String description;
    private final BiConsumer<T, SubAsserter> asserter;

    private AssertingMatcher(
        String description,
        BiConsumer<T, SubAsserter> asserter) {

        this.description = description;
        this.asserter = asserter;
    }

    @Override
    protected boolean matches(Object outerObject, Description description) {
        final AtomicReference<Boolean> failed = new AtomicReference<>(false);

        asserter.accept(
            (T)outerObject,
            new SubAsserter(failed, description));

        return !failed.get();
    }

    @Override
    public void describeTo(Description description) { description.appendText(this.description); }

    public static class SubAsserter {
        private final AtomicReference<Boolean> failed;
        private final Description descriptionToSendFailureText;

        protected SubAsserter(AtomicReference<Boolean> failed, Description descriptionToSendFailureText) {
            this.failed = failed;
            this.descriptionToSendFailureText = descriptionToSendFailureText;
        }

        public <TSubAssert> void assertThat(
            TSubAssert object,
            Matcher<TSubAssert> matcher) {

            if (failed.get())
                return;

            if (!matcher.matches(object)) {
                this.descriptionToSendFailureText.appendDescriptionOf(matcher).appendText(" ");
                matcher.describeMismatch(object, this.descriptionToSendFailureText);

                failed.set(true);
            }
        }
    }
}
