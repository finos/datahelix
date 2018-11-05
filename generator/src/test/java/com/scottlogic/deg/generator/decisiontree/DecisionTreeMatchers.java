package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.LazyMatcher;
import com.scottlogic.deg.generator.MatcherTuple;
import com.scottlogic.deg.generator.constraints.IConstraint;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class DecisionTreeMatchers extends BaseMatcher<List<DecisionTree>> {
    private List<DecisionTree> decisionTrees;
    private FailedMatcher failedMatcher;

    private DecisionTreeMatchers(List<DecisionTree> decisionTrees) {
        this.decisionTrees = decisionTrees;
    }

    public Matcher<Iterable<? extends DecisionTree>> isEquivalentTo() {
        return new LazyMatcher<>(
            "matching decision trees",
            actual ->
                Arrays.asList(
                    new MatcherTuple(
                        containsInAnyOrder(
                            this.decisionTrees
                                .stream()
                                .map(this::isEquivalentTo)
                                .collect(Collectors.toList())), () -> actual)),
            this);
    }

    private Matcher<DecisionTree> isEquivalentTo(DecisionTree expectedTree) {
        return new LazyMatcher<>(
            "matching decision tree: " + expectedTree.toString(),
            actual ->
                Arrays.asList(
                    new MatcherTuple(
                        isEquivalentTo(expectedTree.getRootNode()), actual::getRootNode),
                    new MatcherTuple(
                        containsInAnyOrder(
                            StreamSupport.stream(expectedTree.getFields().spliterator(), true)
                                .toArray(Field[]::new)),
                        actual::getFields)),
            this
        );
    }

    private Matcher<IConstraint> isEquivalentTo(IConstraint expected) {
        return new LazyMatcher<>(
            expected.toString(),
            actual -> Arrays.asList(
                new MatcherTuple(
                    equalTo(expected), () -> actual
                )
            ),
            this
        );
    }

    private Matcher<ConstraintNode> isEquivalentTo(ConstraintNode expected) {
        return new LazyMatcher<>(
            expected.toString(),
            actual ->
                Arrays.asList(
                    new MatcherTuple(equalTo(expected.getAtomicConstraints().size()), () -> actual.getAtomicConstraints().size()),
                    new MatcherTuple(
                        containsInAnyOrder(
                            expected.getAtomicConstraints().stream()
                                .map(this::isEquivalentTo)
                                .collect(Collectors.toList())),
                        actual::getAtomicConstraints),
                    new MatcherTuple(equalTo(expected.getDecisions().size()), () -> actual.getDecisions().size()),
                    new MatcherTuple(
                        containsInAnyOrder(
                            expected.getDecisions().stream()
                                .map(this::isEquivalentTo)
                                .collect(Collectors.toList())),
                        actual::getDecisions)
                ),
            this
        );
    }

    private Matcher<DecisionNode> isEquivalentTo(DecisionNode expected) {
        return new LazyMatcher<>(
            expected.toString(),
            actual ->
                Arrays.asList(
                    new MatcherTuple(equalTo(expected.getOptions().size()), () -> actual.getOptions().size()),
                    new MatcherTuple(
                        containsInAnyOrder(
                            expected.getOptions().stream()
                                .map(this::isEquivalentTo)
                                .collect(Collectors.toList())),
                        actual::getOptions)
                ),
            this
        );
    }

    @Override
    public boolean matches(Object item) {
        Matcher<Iterable<? extends DecisionTree>> matcher = isEquivalentTo();

        return matcher.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        if (this.failedMatcher != null) {
            description.appendText(this.failedMatcher.actualValue.toString());
        }
        else {
            description.appendText("No description available");
        }
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (this.failedMatcher != null) {
            description.appendDescriptionOf(this.failedMatcher.tuple.matcher);
        }
    }

    public static DecisionTreeMatchers isEqualTo(List<DecisionTree> decisionTrees) {
        return new DecisionTreeMatchers(decisionTrees);
    }

    public void thisMatcherFailed(MatcherTuple test, Object actualValue) {
        if (this.failedMatcher != null)
            return;

        this.failedMatcher = new FailedMatcher(test, actualValue);
    }

    class FailedMatcher
    {
        private final MatcherTuple tuple;
        private final Object actualValue;

        public FailedMatcher(MatcherTuple tuple, Object actualValue) {
            this.tuple = tuple;
            this.actualValue = actualValue;
        }
    }
}
