package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.LazyMatcher;
import com.scottlogic.deg.generator.MatcherTuple;
import com.scottlogic.deg.generator.constraints.IConstraint;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
        AtomicInteger treeIndex = new AtomicInteger();

        return new LazyMatcher<>(
            "matching decision trees",
            actual ->
                Arrays.asList(
                    new MatcherTuple(
                        containsInAnyOrder(
                            this.decisionTrees
                                .stream()
                                .map(t -> this.isEquivalentTo(t, treeIndex.getAndIncrement()))
                                .collect(Collectors.toList())), () -> actual, "#Partitions")),
            this);
    }

    private Matcher<DecisionTree> isEquivalentTo(DecisionTree expectedTree, int treeIndex) {
        return new LazyMatcher<>(
            "\\",
            actual ->
                Arrays.asList(
                    new MatcherTuple(
                        isEquivalentTo(expectedTree.getRootNode(), String.format("\\Tree[%d]", treeIndex)), actual::getRootNode),
                    new MatcherTuple(
                        containsInAnyOrder(
                            StreamSupport.stream(expectedTree.getFields().spliterator(), true)
                                .toArray(Field[]::new)),
                        actual::getFields, "#Fields")),
            this
        );
    }

    private Matcher<ConstraintNode> isEquivalentTo(ConstraintNode expected, String path) {
        AtomicInteger decisionIndex = new AtomicInteger();
        AtomicInteger atomicConstraintIndex = new AtomicInteger();

        return new LazyMatcher<>(
            path,
            actual ->
                Arrays.asList(
                    new MatcherTuple(
                        containsInAnyOrder(
                            expected.getAtomicConstraints().stream()
                                .map(ac -> this.isEquivalentTo(ac, String.format("%s\\AtomicConstraint[%d]", path, atomicConstraintIndex.getAndIncrement())))
                                .collect(Collectors.toList())),
                        actual::getAtomicConstraints),
                    new MatcherTuple(
                        containsInAnyOrder(
                            expected.getDecisions().stream()
                                .map(d -> this.isEquivalentTo(d, String.format("%s\\Decision[%d]", path, decisionIndex.getAndIncrement())))
                                .collect(Collectors.toList())),
                        actual::getDecisions)
                ),
            this
        );
    }

    private Matcher<IConstraint> isEquivalentTo(IConstraint expected, String path) {
        return new LazyMatcher<>(
            path,
            actual -> Arrays.asList(
                new MatcherTuple(
                    equalTo(expected), () -> actual, "<AtomicConstraint>"
                )
            ),
            this
        );
    }

    private Matcher<DecisionNode> isEquivalentTo(DecisionNode expected, String path) {
        AtomicInteger index = new AtomicInteger();

        return new LazyMatcher<>(
            path,
            actual ->
                Arrays.asList(
                    new MatcherTuple(
                        containsInAnyOrder(
                            expected.getOptions().stream()
                                .map(option -> this.isEquivalentTo(option, String.format("%s\\Option[%d]", path, index.getAndIncrement())))
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

            String path = this.failedMatcher.description;
            if (this.failedMatcher.tuple.overrideDescription.isPresent())
                path += "\\" + this.failedMatcher.tuple.overrideDescription.orElse("");
            description.appendText("\n\nPath:\n" + path);
        }
    }

    public static DecisionTreeMatchers isEqualTo(List<DecisionTree> decisionTrees) {
        return new DecisionTreeMatchers(decisionTrees);
    }

    public void thisMatcherFailed(MatcherTuple test, Object actualValue, String description) {
        if (this.failedMatcher != null)
            return;

        this.failedMatcher = new FailedMatcher(test, actualValue, description);
    }

    class FailedMatcher
    {
        private final MatcherTuple tuple;
        private final Object actualValue;
        private final String description;

        public FailedMatcher(MatcherTuple tuple, Object actualValue, String description) {
            this.tuple = tuple;
            this.actualValue = actualValue;
            this.description = description;
        }
    }
}
