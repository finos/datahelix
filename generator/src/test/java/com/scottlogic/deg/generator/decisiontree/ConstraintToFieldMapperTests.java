//package com.scottlogic.deg.generator.decisiontree;
//
//import com.scottlogic.deg.generator.Field;
//import com.scottlogic.deg.generator.ProfileFields;
//import com.scottlogic.deg.generator.constraints.IConstraint;
//import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
//import org.hamcrest.Matchers;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class ConstraintToFieldMapperTests {
//    @Test
//    void shouldFindConstraintMappings() {
//        givenFields("A");
//
//        final IConstraint constraint = new IsEqualToConstantConstraint(new Field("A"), "test-value");
//        givenConstraints(constraint);
//        givenFields("A");
//
//        expectMapping(constraint, "A");
//    }
//
//    @Test
//    void shouldFindRootDecisionNodeMapping() {
//        givenFields("B");
//
//        final IConstraint constraint = new IsEqualToConstantConstraint(new Field("B"), "test-value");
//        final DecisionNode decision = new DecisionNode(
//            new ConstraintNode(constraint));
//
//        givenDecisions(decision);
//
//        expectMapping(decision, "B");
//    }
//
//    @Test
//    void shouldCreateCorrectNumberOfMappings() {
//        givenFields("A", "B", "C");
//
//        final IConstraint constraintA = new IsEqualToConstantConstraint(new Field("A"), "test-value");
//        final IConstraint constraintB = new IsEqualToConstantConstraint(new Field("B"), "test-value");
//        final IConstraint constraintC = new IsEqualToConstantConstraint(new Field("C"), "test-value");
//
//        givenConstraints(constraintA, constraintB, constraintC);
//
//        expectMappingCount(3);
//    }
//
//    @Test
//    void shouldMapTopLevelConstraintsToNestedFields() {
//        givenFields("A", "B", "C", "D", "E", "F");
//
//        final IConstraint constraintA = new IsEqualToConstantConstraint(new Field("A"), "test-value");
//        final IConstraint constraintB = new IsEqualToConstantConstraint(new Field("B"), "test-value");
//        final IConstraint constraintC = new IsEqualToConstantConstraint(new Field("C"), "test-value");
//        final IConstraint constraintD = new IsEqualToConstantConstraint(new Field("D"), "test-value");
//        final IConstraint constraintE = new IsEqualToConstantConstraint(new Field("E"), "test-value");
//        final IConstraint constraintF = new IsEqualToConstantConstraint(new Field("F"), "test-value");
//
//        final DecisionNode decisionABC = new DecisionNode(
//            new ConstraintNode(
//                Collections.emptyList(),
//                Arrays.asList(
//                    new DecisionNode(new ConstraintNode(constraintA)),
//                    new DecisionNode(new ConstraintNode(constraintB)),
//                    new DecisionNode(new ConstraintNode(constraintC))
//                )
//            )
//        );
//
//        final DecisionNode decisionDEF = new DecisionNode(
//            new ConstraintNode(
//                Collections.emptyList(),
//                Collections.singletonList(
//                    new DecisionNode(
//                        new ConstraintNode(constraintD),
//                        new ConstraintNode(constraintE),
//                        new ConstraintNode(constraintF))
//                )
//            )
//        );
//
//        givenDecisions(decisionABC, decisionDEF);
//
//        expectMapping(decisionABC, "A", "B", "C");
//        expectMapping(decisionDEF, "D", "E", "F");
//    }
//
//    @Disabled("Nested mappings are currently not supported")
//    @Test
//    void shouldMapNestedConstraintsToNestedFields() {
//        givenFields("A", "B", "C", "D", "E", "F");
//
//        final IConstraint constraintA = new IsEqualToConstantConstraint(new Field("A"), "test-value");
//        final IConstraint constraintB = new IsEqualToConstantConstraint(new Field("B"), "test-value");
//        final IConstraint constraintC = new IsEqualToConstantConstraint(new Field("C"), "test-value");
//        final IConstraint constraintD = new IsEqualToConstantConstraint(new Field("D"), "test-value");
//        final IConstraint constraintE = new IsEqualToConstantConstraint(new Field("E"), "test-value");
//        final IConstraint constraintF = new IsEqualToConstantConstraint(new Field("F"), "test-value");
//
//        final DecisionNode decisionB = new DecisionNode(new ConstraintNode(constraintB));
//
//        final DecisionNode decisionABC = new DecisionNode(
//            new ConstraintNode(
//                Collections.emptyList(),
//                Arrays.asList(
//                    new DecisionNode(new ConstraintNode(constraintA)),
//                    decisionB,
//                    new DecisionNode(new ConstraintNode(constraintC))
//                )
//            )
//        );
//
//        final DecisionNode decisionDEF = new DecisionNode(
//            new ConstraintNode(
//                Collections.emptyList(),
//                Collections.singletonList(
//                    new DecisionNode(
//                        new ConstraintNode(constraintD),
//                        new ConstraintNode(constraintE),
//                        new ConstraintNode(constraintF))
//                )
//            )
//        );
//
//        givenDecisions(decisionABC, decisionDEF);
//
//        expectMapping(constraintA, "A");
//        expectMapping(constraintB, "B");
//        expectMapping(constraintC, "C");
//        expectMapping(constraintD, "D");
//        expectMapping(constraintE, "E");
//        expectMapping(constraintF, "F");
//        expectMapping(decisionB, "B");
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        constraintsList = new ArrayList<>();
//        decisionsList = new ArrayList<>();
//        fields = new ProfileFields(Collections.emptyList());
//        mappings = null;
//    }
//
//    private List<IConstraint> constraintsList;
//    private List<DecisionNode> decisionsList;
//    private ProfileFields fields;
//    private Map<Object, Set<Field>> mappings;
//
//    void givenConstraints(IConstraint... constraints) {
//        constraintsList = Arrays.asList(constraints);
//    }
//
//    void givenDecisions(DecisionNode... decisions) {
//        decisionsList = Arrays.asList(decisions);
//    }
//
//    void givenFields(String... fieldNames) {
//        fields = new ProfileFields(
//            Arrays.stream(fieldNames)
//                .map(Field::new)
//                .collect(Collectors.toList()));
//    }
//
//    void getMapppings() {
//        mappings = new ConstraintToFieldMapper()
//            .mapConstraintsToFields(new DecisionTree(
//                new ConstraintNode(constraintsList, decisionsList),
//                fields
//            ));
//    }
//
//    void expectMapping(IConstraint constraint, String... fieldsAsString) {
//        if (mappings == null)
//            getMapppings();
//
//        final Field[] fields = Arrays.stream(fieldsAsString)
//            .map(Field::new)
//            .toArray(Field[]::new);
//
//        Assert.assertThat(mappings.get(constraint), Matchers.hasItems(fields));
//    }
//
//    void expectMapping(DecisionNode decision, String... fieldsAsString) {
//        if (mappings == null)
//            getMapppings();
//
//        final Field[] fields = Arrays.stream(fieldsAsString)
//            .map(Field::new)
//            .toArray(Field[]::new);
//
//        Assert.assertThat(mappings.get(decision), Matchers.hasItems(fields));
//    }
//
//    void expectMappingCount(int mappingsCount) {
//        if (mappings == null)
//            getMapppings();
//
//        Assert.assertThat(mappings, Matchers.aMapWithSize(mappingsCount));
//    }
//}
