package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeDecisionNode;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

class ConstraintToFieldMapperTests {
    @Test
    void shouldFindConstraintMappings() {
        givenFields("A");

        final AtomicConstraint constraint = new IsEqualToConstantConstraint(new Field("A"), "test-value");
        givenConstraints(constraint);
        givenFields("A");

        expectMapping(constraint, "A");
    }

    @Test
    void shouldFindRootDecisionNodeMapping() {
        givenFields("B");

        final AtomicConstraint constraint = new IsEqualToConstantConstraint(new Field("B"), "test-value");
        final DecisionNode decision = new TreeDecisionNode(
            new TreeConstraintNode(constraint));

        givenDecisions(decision);

        expectMapping(decision, "B");
    }

    @Test
    void shouldCreateCorrectNumberOfMappings() {
        givenFields("A", "B", "C");

        final AtomicConstraint constraintA = new IsEqualToConstantConstraint(new Field("A"), "test-value");
        final AtomicConstraint constraintB = new IsEqualToConstantConstraint(new Field("B"), "test-value");
        final AtomicConstraint constraintC = new IsEqualToConstantConstraint(new Field("C"), "test-value");

        givenConstraints(constraintA, constraintB, constraintC);

        expectMappingCount(3);
    }

    @Test
    void shouldMapTopLevelConstraintsToNestedFields() {
        givenFields("A", "B", "C", "D", "E", "F");

        final AtomicConstraint constraintA = new IsEqualToConstantConstraint(new Field("A"), "test-value");
        final AtomicConstraint constraintB = new IsEqualToConstantConstraint(new Field("B"), "test-value");
        final AtomicConstraint constraintC = new IsEqualToConstantConstraint(new Field("C"), "test-value");
        final AtomicConstraint constraintD = new IsEqualToConstantConstraint(new Field("D"), "test-value");
        final AtomicConstraint constraintE = new IsEqualToConstantConstraint(new Field("E"), "test-value");
        final AtomicConstraint constraintF = new IsEqualToConstantConstraint(new Field("F"), "test-value");

        final DecisionNode decisionABC = new TreeDecisionNode(
            new TreeConstraintNode(
                Collections.emptyList(),
                Arrays.asList(
                    new TreeDecisionNode(new TreeConstraintNode(constraintA)),
                    new TreeDecisionNode(new TreeConstraintNode(constraintB)),
                    new TreeDecisionNode(new TreeConstraintNode(constraintC))
                )
            )
        );

        final DecisionNode decisionDEF = new TreeDecisionNode(
            new TreeConstraintNode(
                Collections.emptyList(),
                Collections.singletonList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(constraintD),
                        new TreeConstraintNode(constraintE),
                        new TreeConstraintNode(constraintF))
                )
            )
        );

        givenDecisions(decisionABC, decisionDEF);

        expectMapping(decisionABC, "A", "B", "C");
        expectMapping(decisionDEF, "D", "E", "F");
    }

    @BeforeEach
    void beforeEach() {
        constraintsList = new ArrayList<>();
        decisionsList = new ArrayList<>();
        fields = new ProfileFields(Collections.emptyList());
        mappings = null;
    }

    private List<AtomicConstraint> constraintsList;
    private List<DecisionNode> decisionsList;
    private ProfileFields fields;
    private Map<RootLevelConstraint, Set<Field>> mappings;

    private void givenConstraints(AtomicConstraint... constraints) {
        constraintsList = Arrays.asList(constraints);
    }

    private void givenDecisions(DecisionNode... decisions) {
        decisionsList = Arrays.asList(decisions);
    }

    private void givenFields(String... fieldNames) {
        fields = new ProfileFields(
            Arrays.stream(fieldNames)
                .map(Field::new)
                .collect(Collectors.toList()));
    }

    private void getMappings() {
        mappings = new ConstraintToFieldMapper()
            .mapConstraintsToFields(new DecisionTree(
                new TreeConstraintNode(constraintsList, decisionsList),
                fields,
                "Decision Tree"
            ));
    }

    private void expectMapping(AtomicConstraint constraint, String... fieldsAsString) {
        if (mappings == null)
            getMappings();

        final Field[] fields = Arrays.stream(fieldsAsString)
            .map(Field::new)
            .toArray(Field[]::new);

        Assert.assertThat(mappings.get(new RootLevelConstraint(constraint)), Matchers.hasItems(fields));
    }

    private void expectMapping(DecisionNode decision, String... fieldsAsString) {
        if (mappings == null)
            getMappings();

        final Field[] fields = Arrays.stream(fieldsAsString)
            .map(Field::new)
            .toArray(Field[]::new);

        Assert.assertThat(mappings.get(new RootLevelConstraint(decision)), Matchers.hasItems(fields));
    }

    private void expectMappingCount(int mappingsCount) {
        if (mappings == null)
            getMappings();

        Assert.assertThat(mappings, Matchers.aMapWithSize(mappingsCount));
    }
}
