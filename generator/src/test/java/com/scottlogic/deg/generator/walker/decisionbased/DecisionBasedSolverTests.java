package com.scottlogic.deg.generator.walker.decisionbased;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.builders.TestConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.NullDistributedSet;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.MatcherAssert.assertThat;

class DecisionBasedSolverTests {
    private Field fieldA = new Field("A");
    private Field fieldB = new Field("B");
    private List<Field> fields = new ArrayList<>();
    private ProfileFields profileFields;
    private ConstraintReducer constraintReducer;
    private ReductiveTreePruner pruner;
    private OptionPicker optionPicker;
    private DecisionBasedSolver decisionBasedSolver;

    @BeforeEach
    void setup() {
        fields.add(fieldA);
        fields.add(fieldB);
        profileFields = new ProfileFields(fields);

        constraintReducer = new ConstraintReducer(new FieldSpecFactory(new StringRestrictionsFactory()), new FieldSpecMerger());
        pruner = new ReductiveTreePruner(new FieldSpecMerger(), constraintReducer, new FieldSpecHelper());
        optionPicker = new SequentialOptionPicker();
        decisionBasedSolver = new DecisionBasedSolver(constraintReducer, pruner, optionPicker);
    }

    @Test
    void createRowSpecs_whenRootNodeHasNoDecisions_returnsRowSpecOfRoot() {
        //Arrange
        ConstraintNode root = TestConstraintNodeBuilder.constraintNode().build();
        DecisionTree tree = new DecisionTree(root, profileFields);

        //Act
        Stream<RowSpec> rowSpecs = decisionBasedSolver.createRowSpecs(tree);

        //Assert
        List<RowSpec> expectedRowSpecs = new ArrayList<>();
        Map<Field, FieldSpec> fieldToFieldSpec = new HashMap<>();
        fieldToFieldSpec.put(fieldA, FieldSpec.Empty);
        fieldToFieldSpec.put(fieldB, FieldSpec.Empty);
        expectedRowSpecs.add(new RowSpec(profileFields, fieldToFieldSpec));

        assertThat(expectedRowSpecs, sameBeanAs(rowSpecs.collect(Collectors.toList())));
    }

    @Test
    void createRowSpecs_whenRootNodeHasNoDecisionsButSomeConstraints_returnsRowSpecOfRoot() {
        //Arrange
        ConstraintNode root = TestConstraintNodeBuilder.constraintNode().where(fieldA).isInSet(1, 2, 3).build();
        DecisionTree tree = new DecisionTree(root, profileFields);

        //Act
        Stream<RowSpec> rowSpecs = decisionBasedSolver.createRowSpecs(tree);

        //Assert
        List<RowSpec> expectedRowSpecs = new ArrayList<>();
        Map<Field, FieldSpec> fieldToFieldSpec = new HashMap<>();
        DistributedSet<Object> whitelist = new NullDistributedSet<>();
        fieldToFieldSpec.put(fieldA, FieldSpec.Empty.withWhitelist(whitelist));
        fieldToFieldSpec.put(fieldB, FieldSpec.Empty);
        expectedRowSpecs.add(new RowSpec(profileFields, fieldToFieldSpec));

        assertThat(expectedRowSpecs, sameBeanAs(rowSpecs.collect(Collectors.toList())));
    }

    @Test
    void createRowSpecs_whenRootNodeHasSomeDecisions_returnsRowSpecOfRoot() {
        //Arrange
        ConstraintNode root = TestConstraintNodeBuilder.constraintNode()
            .withDecision(
                TestConstraintNodeBuilder.constraintNode()
                    .where(fieldB).isNull(),
                TestConstraintNodeBuilder.constraintNode()
                    .where(fieldB).isInSet(1, 2, 3))
            .build();
        DecisionTree tree = new DecisionTree(root, profileFields);

        //Act
        Stream<RowSpec> rowSpecs = decisionBasedSolver.createRowSpecs(tree);

        //Assert
        List<RowSpec> expectedRowSpecs = new ArrayList<>();
        Map<Field, FieldSpec> option0 = new HashMap<>();
        DistributedSet<Object> whitelist = new NullDistributedSet<>();
        option0.put(fieldA, FieldSpec.Empty);
        option0.put(fieldB, FieldSpec.Empty.withWhitelist(whitelist));
        expectedRowSpecs.add(new RowSpec(profileFields, option0));
        Map<Field, FieldSpec> option1 = new HashMap<>();
        option1.put(fieldA, FieldSpec.Empty);
        option1.put(fieldB, FieldSpec.NullOnly);
        expectedRowSpecs.add(new RowSpec(profileFields, option1));

        assertThat(expectedRowSpecs, sameBeanAs(rowSpecs.collect(Collectors.toList())));
    }
}
