package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.scottlogic.deg.generator.AssertingMatcher.matchesAssertions;
import static org.hamcrest.Matchers.*;

class TreePartitionerTests {
    @Test
    void shouldSplitTreeIntoPartitions() {
        givenTree(
            tree(fields("A", "B", "C", "D", "E", "F"),
                constraint(
                    decision(
                        constraint("A"),
                        constraint("B")
                    ),
                    decision(
                        constraint("C"),
                        constraint("D")
                    ),
                    decision(
                        constraint("E"),
                        constraint("F")
                    )
        )));

        expectTrees(
            tree(fields("A", "B"),
                constraint(
                    decision(
                        constraint("A"),
                        constraint("B")
                    ))),
            tree(fields("C", "D"),
                constraint(
                    decision(
                        constraint("C"),
                        constraint("D")
                    ))),
            tree(fields("E", "F"),
                constraint(
                    decision(
                        constraint("E"),
                        constraint("F")
                    ))
        ));
    }

    @Test
    void shouldPartitionTwoNodesCorrectly() {
        givenTree(
            tree(fields("A", "B", "C", "D", "E", "F"),
                constraint(
                    decision(
                        constraint("A"),
                        constraint("B"),
                        constraint("E")
                    ),
                    decision(
                        constraint("C"),
                        constraint("D")
                    ),
                    decision(
                        constraint("E"),
                        constraint("F")
                    )
        )));

        expectTrees(
            tree(fields("A", "B", "E", "F"),
                constraint(
                    decision(
                        constraint("A"),
                        constraint("B"),
                        constraint("E")
                    ),
                    decision(
                        constraint("E"),
                        constraint("F")
                    ))),
            tree(fields("C", "D"),
                constraint(
                    decision(
                        constraint("C"),
                        constraint("D")
                    ))
            ));
    }

    @Test
    void shouldNotPartition() {
        givenTree(
            tree(fields("A", "B", "C", "D", "E", "F", "G"),
                constraint(
                    decision(
                        constraint("A"),
                        constraint("B"),
                        constraint("C")
                    ),
                    decision(
                        constraint("C"),
                        constraint("D"),
                        constraint("E")
                    ),
                    decision(
                        constraint("E"),
                        constraint("F"),
                        constraint("G")
                    )
        )));

        expectTrees(
            tree(fields("A", "B", "C", "D", "E", "F", "G"),
                constraint(
                    decision(
                        constraint("A"),
                        constraint("B"),
                        constraint("C")
                    ),
                    decision(
                        constraint("C"),
                        constraint("D"),
                        constraint("E")
                    ),
                    decision(
                        constraint("E"),
                        constraint("F"),
                        constraint("G")
                    )
                )
            ));
    }

    @Test
    void shouldPartitionConstraintsCorrectly() {
        givenTree(
            tree(fields("A", "B", "C"),
                constraint(
                    new String[] {"A", "B", "C"},
                    decision(constraint("A")),
                    decision(constraint("B")),
                    decision(constraint("C"))
                )
        ));

        expectTrees(
            tree(fields("A"),
                constraint(
                    new String[] {"A"},
                    decision(constraint("A"))
                )),
            tree(fields("B"),
                constraint(
                    new String[] {"B"},
                    decision(constraint("B"))
                )),
            tree(fields("C"),
                constraint(
                    new String[] {"C"},
                    decision(constraint("C"))
                ))
        );
    }

    @Test
    void shouldNotErrorIfFieldsNotConstrained() {
        givenTree(
            tree(fields("A", "B"),
                constraint("A")));

        expectTrees(
            tree(fields("A"),
                constraint("A")),
            tree(fields("B"),
                new ConstraintNode()));
    }

    @Test
    void shouldNotErrorIfNoFieldsConstrained() {
        givenTree(
            tree(fields("A", "B", "C"),
                new ConstraintNode()));

        expectTrees(
            tree(fields("A"), new ConstraintNode()),
            tree(fields("B"), new ConstraintNode()),
            tree(fields("C"), new ConstraintNode()));
    }

    private ConstraintNode constraint(String... fieldNames) {
        return constraint(fieldNames, new DecisionNode[0]);
    }

    private ConstraintNode constraint(DecisionNode... decisions) {
        return constraint(new String[0], decisions);
    }

    private ConstraintNode constraint(String[] fieldNames, DecisionNode... decisions) {
        return new ConstraintNode(
            Stream.of(fieldNames)
                .map(this::atomicConstraint)
                .collect(Collectors.toList()),
            Arrays.asList(decisions));
    }

    private IConstraint atomicConstraint(String fieldName) {
        IConstraint constraint = this.constraints.get(fieldName);

        if (constraint == null) {
            constraint = new IsEqualToConstantConstraint(new Field(fieldName), "sample-value");
            this.constraints.put(fieldName, constraint);
        }

        return constraint;
    }

    private DecisionNode decision(ConstraintNode... constraints) {
        return new DecisionNode(constraints);
    }

    private ProfileFields fields(String... fieldNames) {
        return new ProfileFields(
            Stream.of(fieldNames)
                .map(Field::new)
                .collect(Collectors.toList()));
    }

    private DecisionTree tree(ProfileFields fields, ConstraintNode rootNode) {
        return new DecisionTree(rootNode, fields);
    }

    @BeforeEach
    void beforeEach() {
        constraints = new HashMap<>();
        decisionTree = null;
        partitionedTrees = null;
    }

    private Map<String, IConstraint> constraints;
    private List<DecisionTree> partitionedTrees;
    private DecisionTree decisionTree;

    private void givenTree(DecisionTree decisionTree) {
        this.decisionTree = decisionTree;
    }

    private void partitionTrees() {
        partitionedTrees = new TreePartitioner()
            .splitTreeIntoPartitions(decisionTree)
            .collect(Collectors.toList());
    }
    private void expectTrees(DecisionTree... decisionTrees) {
        if (partitionedTrees == null)
            partitionTrees();

        Assert.assertThat(
            partitionedTrees,
            isEquivalentTo(Arrays.asList(decisionTrees))
        );
    }

    // TODO: Code from here down is copied from DataGeneratorTests.java, move them to some common place
    private Matcher<List<DecisionTree>> isEquivalentTo(List<DecisionTree> expectedTrees) {
        return matchesAssertions(
            "matching decision trees",
            (actual, asserter) -> {
                asserter.assertThat(
                    actual,
                    containsInAnyOrder(
                        expectedTrees
                            .stream()
                            .map(this::isEquivalentTo)
                            .collect(Collectors.toList())));
            }
        );
    }

    private Matcher<DecisionTree> isEquivalentTo(DecisionTree expectedTree) {
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

    private Matcher<ConstraintNode> isEquivalentTo(ConstraintNode expected) {
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
                            .map(c -> anyOf(sameInstance(c), sameNegation(c)))
                            .collect(Collectors.toList())));

                asserter.assertThat( // Should have same number of decisions
                    actual.getDecisions().size(),
                    equalTo(expected.getDecisions().size()));

                asserter.assertThat( // Should have same decisions
                    actual.getDecisions(),
                    containsInAnyOrder(
                            expected.getDecisions().stream()
                            .map(this::isEquivalentTo)
//                            .map(option -> isEquivalentTo(option))
                            .collect(Collectors.toList())));
            });
    }

    private Matcher<DecisionNode> isEquivalentTo(DecisionNode expected) {
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
                            .map(this::isEquivalentTo)
                            .collect(Collectors.toList())));
            });
    }

    private Matcher<IConstraint> sameNegation(IConstraint expected) {
        return matchesAssertions(
            "Both constraints negate the same constraint instance",
            (actual, subAssert) -> {
                subAssert.assertThat(actual, instanceOf(NotConstraint.class));
                subAssert.assertThat(expected, instanceOf(NotConstraint.class));

                if (!(actual instanceof NotConstraint && expected instanceof NotConstraint))
                    return;

                subAssert.assertThat(
                    ((NotConstraint) actual).negatedConstraint,
                    sameInstance(((NotConstraint)expected).negatedConstraint));
            });
    }
}
