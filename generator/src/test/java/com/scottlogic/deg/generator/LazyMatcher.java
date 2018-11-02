package com.scottlogic.deg.generator;

import javafx.util.Pair;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LazyMatcher<T> extends BaseMatcher<T> {
    private final Function<T, List<MatcherTuple>> tests;
    private final String description;
    private List<Object> failedMatches;

    public LazyMatcher(
        String description,
        Function<T, List<MatcherTuple>> tests,
        List<Object> failedMatches) {

        this.description = description;
        this.tests = tests;
        this.failedMatches = failedMatches;
    }

//    @Override
//    public boolean matches(Object actualObject) {
//        ArrayList<Object> currentOccurenceFailedMatches = new ArrayList<>();
//
//        for (MatcherTuple test : this.tests.apply((T)actualObject)) {
//            final Object actualValue = test.getActualFunc.get();
//            if (!test.matcher.matches(actualValue))
//                currentOccurenceFailedMatches.add(test);
//        }
//
//        this.failedMatches.addAll(currentOccurenceFailedMatches);
//
//        return currentOccurenceFailedMatches.isEmpty();
//    }

    @Override
    public boolean matches(Object actualObject) {
        boolean containsFailedMatch = false;

        for (MatcherTuple test : this.tests.apply((T)actualObject)) {
            final Object actualValue = test.getActualFunc.get();
            boolean result = test.matcher.matches(actualValue);
            if (!result) {
                containsFailedMatch = true;
            }

            if (!result && test.matcher instanceof IsIterableContainingInAnyOrder)
                this.failedMatches.add(test);
        }

        return !containsFailedMatch;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
//        for (MatcherTuple match : this.failedMatches) {
//            String test = "";
//            //description.appendDescriptionOf(matcherPair.getKey());
//        }
    }
}

