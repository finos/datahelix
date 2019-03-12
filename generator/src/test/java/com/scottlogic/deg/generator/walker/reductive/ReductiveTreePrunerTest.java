package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class ReductiveTreePrunerTest {

    ReductiveTreePruner treePruner = new ReductiveTreePruner(new FieldSpecMerger(), new ConstraintReducer(new FieldSpecFactory(new FieldSpecMerger()), new FieldSpecMerger()));
    Field field1 = new Field("foo");
    NoopDataGeneratorMonitor noopMonitor = new NoopDataGeneratorMonitor();

    // SINGLE LAYER

        // CONSTRAINT PRUNE:

        // Leaf constraint -> contradicts with last fixed field

    // Leaf constraint -> No contradictions with last fixed field
    @Test
    public void pruneConstraintNode_leafNodeNoContradictions_returnsLeafNode() {
        //Arrange
        ConstraintNode currentConstraintNode = new TreeConstraintNode(new IsLessThanConstantConstraint(field1, 5, Collections.EMPTY_SET));
        FieldSpec FixedFieldSpec = FieldSpec.Empty;
        FixedField fixedField = new FixedField(field1, Stream.empty(), FixedFieldSpec, noopMonitor);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(currentConstraintNode, fixedField).get();

        //Assert
        ConstraintNode expected = currentConstraintNode;
        assertThat(actual, sameBeanAs(expected));
    }

        // With a decision -> Decision contradicts

        // With a decision -> No contradictions, multiple remaining options

        // With a decision -> One contradiction, one remaining option

        // With a decision -> Two or more contradictions, one remaining option


        // DECISION PRUNE

        // Both constraints contradict

        // One constraint contradicts

        // No constraints contradict


    // MULTI-LAYERED

    // Each layer has one contradiction -> prunes correctly

    // Only one layer has contradiction -> prunes correctly

    // Both layers contradict -> returns invalid tree

    // No contradictions in any layer -> same tree returned

}