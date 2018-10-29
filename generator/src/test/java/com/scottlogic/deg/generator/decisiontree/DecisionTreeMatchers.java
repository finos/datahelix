package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.scottlogic.deg.generator.AssertingMatcher.matchesAssertions;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class DecisionTreeMatchers {
    public static Matcher<List<DecisionTree>> isEquivalentTo(List<DecisionTree> expectedTrees) {
        return matchesAssertions(
            "matching decision trees",
            (actual, asserter) -> {
                asserter.assertThat(
                    actual,
                    containsInAnyOrder(
                        expectedTrees
                            .stream()
                            .map(DecisionTreeMatchers::isEquivalentTo)
                            .collect(Collectors.toList())));
            }
        );
    }

    public static Matcher<DecisionTree> isEquivalentTo(DecisionTree expectedTree) {
        return matchesAssertions(
            "matching decision tree",
            (actual, asserter) -> {
                asserter.assertThat(actual.getRootNode(), isEquivalentTo(expectedTree.getRootNode()));

                asserter.assertThat(
                    actual.getFields(),
                    containsInAnyOrder(
                        StreamSupport.stream(expectedTree.getFields().spliterator(), true)
                            .toArray(Field[]::new)));
            }
        );
    }

    public static Matcher<ConstraintNode> isEquivalentTo(ConstraintNode expected) {
        return matchesAssertions(
            "matching option node",
            (actual, asserter) -> {
                asserter.assertThat( // Should have same number of atomic constraints
                    actual.getAtomicConstraints().size(),
                    equalTo(expected.getAtomicConstraints().size()));

                asserter.assertThat( // Should have same atomic constraints
                    actual.getAtomicConstraints(),
                    containsInAnyOrder(
                        expected.getAtomicConstraints().stream()
                            .map(CoreMatchers::equalTo)
                            .collect(Collectors.toList())));

                asserter.assertThat( // Should have same number of decisions
                    actual.getDecisions().size(),
                    equalTo(expected.getDecisions().size()));

                asserter.assertThat( // Should have same decisions
                    actual.getDecisions(),
                    containsInAnyOrder(
                        expected.getDecisions().stream()
                            .map(DecisionTreeMatchers::isEquivalentTo)
                            .collect(Collectors.toList())));
            });
    }

    private static Matcher<DecisionNode> isEquivalentTo(DecisionNode expected) {
        return matchesAssertions(
            "matching decision node",
            (actual, asserter) -> {
                asserter.assertThat( // Should have same number of options
                    actual.getOptions().size(),
                    equalTo(expected.getOptions().size()));

                asserter.assertThat( // Should have same options
                    actual.getOptions(),
                    containsInAnyOrder(
                        expected.getOptions().stream()
                            .map(DecisionTreeMatchers::isEquivalentTo)
                            .collect(Collectors.toList())));
            });
    }
}
