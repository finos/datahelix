package com.scottlogic.deg.generator;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AssertingMatcher<T> extends DiagnosingMatcher<T> {
    public static <U> Matcher<U> matchesAssertions(
        String description,
        BiConsumer<U, SubAsserter> asserter,
        Consumer<AssertingMatcher> recordFailure) {
        return new AssertingMatcher<>(description, asserter, recordFailure);
    }

    private final String description;
    private final BiConsumer<T, SubAsserter> asserter;
    private final Consumer<AssertingMatcher> recordFailure;

    private AssertingMatcher(
        String description,
        BiConsumer<T, SubAsserter> asserter, Consumer<AssertingMatcher> recordFailure) {

        this.description = description;
        this.asserter = asserter;
        this.recordFailure = recordFailure;
    }

    @Override
    protected boolean matches(Object outerObject, Description description) {
        final AtomicReference<Boolean> failed = new AtomicReference<>(false);

        final SubAsserter subAsserter = new SubAsserter(failed, this.description);
        asserter.accept(
            (T)outerObject,
            subAsserter);

        if (failed.get()) {
            recordFailure.accept(this);
        }

        return !failed.get();
    }

    @Override
    public void describeTo(Description description) { description.appendText(this.description); }

    public static class SubAsserter {
        private final AtomicReference<Boolean> failed;
        private final String description;
        //private final Description descriptionToSendFailureText;

        protected SubAsserter(AtomicReference<Boolean> failed, String description) {
            this.failed = failed;
            //this.descriptionToSendFailureText = descriptionToSendFailureText;
            this.description = description;
        }

        public <TSubAssert> void assertThat(
            TSubAssert object,
            Matcher<TSubAssert> matcher) {

            if (failed.get())
                return;

            if (!matcher.matches(object)) {
                /*this.descriptionToSendFailureText.appendDescriptionOf(matcher).appendText(" ");
                matcher.describeMismatch(object, this.descriptionToSendFailureText);*/

                failed.set(true);
            }
        }/*

        public void describeMismatch(final Object item, Description description){
            description.appendText(this.description);
        }*/
    }
}










