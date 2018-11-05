package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.DecisionTreeMatchers;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.List;
import java.util.function.Function;

public class LazyMatcher<T> extends BaseMatcher<T> {
    private final Function<T, List<MatcherTuple>> tests;
    private final String description;
    private final DecisionTreeMatchers rootMatcher;

    public LazyMatcher(
        String description,
        Function<T, List<MatcherTuple>> tests,
        DecisionTreeMatchers rootMatcher) {

        this.description = description;
        this.tests = tests;
        this.rootMatcher = rootMatcher;
    }

    @Override
    public boolean matches(Object actualObject) {
        boolean containsFailedMatch = false;

        for (MatcherTuple test : this.tests.apply((T)actualObject)) {
            final Object actualValue = test.getActualFunc.get();
            boolean result = test.matcher.matches(actualValue);
            if (!result) {
                containsFailedMatch = true;
                rootMatcher.thisMatcherFailed(this, actualObject);
            }
        }

        return !containsFailedMatch;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText(this.description);
    }
}

