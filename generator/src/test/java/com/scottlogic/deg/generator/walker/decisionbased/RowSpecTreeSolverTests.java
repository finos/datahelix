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
import com.scottlogic.deg.generator.walker.pruner.TreePruner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class RowSpecTreeSolverTests {
    private Field fieldA = createField("A");
    private Field fieldB = createField("B");
    private List<Field> fields = new ArrayList<>();
    private ProfileFields profileFields;
    private ConstraintReducer constraintReducer;
    private TreePruner pruner;
    private OptionPicker optionPicker;
    private RowSpecTreeSolver rowSpecTreeSolver;

    @BeforeEach
    void setup() {
        fields.add(fieldA);
        fields.add(fieldB);
        profileFields = new ProfileFields(fields);

        constraintReducer = new ConstraintReducer(new FieldSpecFactory(new StringRestrictionsFactory()), new FieldSpecMerger());
        pruner = new TreePruner(new FieldSpecMerger(), constraintReducer, new FieldSpecHelper());
        optionPicker = new SequentialOptionPicker();
        rowSpecTreeSolver = new RowSpecTreeSolver(constraintReducer, pruner, optionPicker);
    }

    @Test
    void createRowSpecs_whenRootNodeHasNoDecisions_returnsRowSpecOfRoot() {
        //Arrange
        ConstraintNode root = TestConstraintNodeBuilder.constraintNode().build();
        DecisionTree tree = new DecisionTree(root, profileFields);

        //Act
        Stream<RowSpec> rowSpecs = rowSpecTreeSolver.createRowSpecs(tree);

        //Assert
        List<RowSpec> expectedRowSpecs = new ArrayList<>();
        Map<Field, FieldSpec> fieldToFieldSpec = new HashMap<>();
        fieldToFieldSpec.put(fieldA, FieldSpec.Empty);
        fieldToFieldSpec.put(fieldB, FieldSpec.Empty);
        expectedRowSpecs.add(new RowSpec(profileFields, fieldToFieldSpec, Collections.emptyList()));

        assertThat(expectedRowSpecs, sameBeanAs(rowSpecs.collect(Collectors.toList())));
    }

    @Test
    void createRowSpecs_whenRootNodeHasNoDecisionsButSomeConstraints_returnsRowSpecOfRoot() {
        //Arrange
        ConstraintNode root = TestConstraintNodeBuilder.constraintNode().where(fieldA).isInSet(1, 2, 3).build();
        DecisionTree tree = new DecisionTree(root, profileFields);

        //Act
        Stream<RowSpec> rowSpecs = rowSpecTreeSolver.createRowSpecs(tree);

        //Assert
        List<RowSpec> expectedRowSpecs = new ArrayList<>();
        Map<Field, FieldSpec> fieldToFieldSpec = new HashMap<>();
        DistributedSet<Object> whitelist = new NullDistributedSet<>();
        fieldToFieldSpec.put(fieldA, FieldSpec.Empty.withWhitelist(whitelist));
        fieldToFieldSpec.put(fieldB, FieldSpec.Empty);
        expectedRowSpecs.add(new RowSpec(profileFields, fieldToFieldSpec, Collections.emptyList()));

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
        Stream<RowSpec> rowSpecs = rowSpecTreeSolver.createRowSpecs(tree);

        //Assert
        List<RowSpec> expectedRowSpecs = new ArrayList<>();
        Map<Field, FieldSpec> option0 = new HashMap<>();
        DistributedSet<Object> whitelist = new NullDistributedSet<>();
        option0.put(fieldA, FieldSpec.Empty);
        option0.put(fieldB, FieldSpec.Empty.withWhitelist(whitelist));
        expectedRowSpecs.add(new RowSpec(profileFields, option0, Collections.emptyList()));
        Map<Field, FieldSpec> option1 = new HashMap<>();
        option1.put(fieldA, FieldSpec.Empty);
        option1.put(fieldB, FieldSpec.NullOnly);
        expectedRowSpecs.add(new RowSpec(profileFields, option1, Collections.emptyList()));

        assertThat(expectedRowSpecs, sameBeanAs(rowSpecs.collect(Collectors.toList())));
    }
}
