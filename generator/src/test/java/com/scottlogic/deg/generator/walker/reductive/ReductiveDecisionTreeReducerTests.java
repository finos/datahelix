package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

class ReductiveDecisionTreeReducerTests {
    private static final Field field1 = new Field("field1");
    private static final Field field2 = new Field("field2");
    private static final Field field3 = new Field("field3");
    private static final ProfileFields allFields = new ProfileFields(Arrays.asList(field1, field2, field3));
    private static final ReductiveState initialState = new ReductiveState(allFields);
    private static final AtomicConstraint isEqualTo1 = new IsEqualToConstantConstraint(field1, 1, Collections.emptySet());
    private static final AtomicConstraint isGreaterThanOrEqualTo2 = new IsGreaterThanOrEqualToConstantConstraint(field2, 2, Collections.emptySet());
    private static final AtomicConstraint isLessThan2 = new IsLessThanConstantConstraint(field3, 2, Collections.emptySet());

    private static final FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();
    private static final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private static final DecisionTreeSimplifier simplifier = new DecisionTreeSimplifier();
    private static final ReductiveDataGeneratorMonitor nullMonitor = new NoopDataGeneratorMonitor();

    @Test
    public void shouldNoOpReduceEmptyTree(){
        ConstraintNode tree = TreeConstraintNode.empty;
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);

        ReductiveConstraintNode reduced = reducer.reduce(tree, initialState);

        Assert.assertThat(reduced.getAtomicConstraints(), equalTo(Collections.emptySet()));
        Assert.assertThat(reduced.getDecisions(), equalTo(Collections.emptySet()));
        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            empty());
    }

    @Test
    public void shouldNoOpReduceTreeWithInitialState(){
        DecisionNode decision1 = new TreeDecisionNode(
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);

        ReductiveConstraintNode reduced = reducer.reduce(tree, initialState);

        Assert.assertThat(reduced.getAtomicConstraints(), containsInAnyOrder(isEqualTo1));
        Assert.assertThat(reduced.getDecisions().size(), equalTo(1));
        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            containsInAnyOrder(isEqualTo1, isGreaterThanOrEqualTo2, isLessThan2));
    }

    @Test
    public void shouldRemoveConstraintsThatContradictFixedFields(){
        DecisionNode decision1 = new TreeDecisionNode(
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field2FixedTo0 = new FixedField(field2, Stream.of(0), FieldSpec.Empty, nullMonitor);
        ReductiveState field2Fixed = initialState.with(field2FixedTo0);
        field2FixedTo0.getStream().iterator().next(); //move to the first value for field2, i.e. 0

        ReductiveConstraintNode reduced = reducer.reduce(tree, field2Fixed);

        Assert.assertThat(reduced.getAtomicConstraints(), containsInAnyOrder(isEqualTo1, isLessThan2));
        Assert.assertThat(reduced.getDecisions().isEmpty(), equalTo(true));
        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            containsInAnyOrder(isEqualTo1, isLessThan2));
        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            not(containsInAnyOrder(isGreaterThanOrEqualTo2)));
    }

    @Test
    public void shouldLeaveConstraintsThatComplementFixedFields(){
        DecisionNode decision1 = new TreeDecisionNode(
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field1FixedTo1 = new FixedField(field1, Stream.of(1), FieldSpec.Empty, nullMonitor);
        ReductiveState field1Fixed = initialState.with(field1FixedTo1);
        field1FixedTo1.getStream().iterator().next(); //move to the first value for field1, i.e. 1

        ReductiveConstraintNode reduced = reducer.reduce(tree, field1Fixed);

        Assert.assertThat(reduced.getAtomicConstraints(), containsInAnyOrder(isEqualTo1));
        Assert.assertThat(reduced.getDecisions().size(), equalTo(1));
        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            containsInAnyOrder(isEqualTo1, isGreaterThanOrEqualTo2, isLessThan2));
    }

    @Test
    public void shouldLeaveConstraintsThatAreNotFixed(){
        DecisionNode decision1 = new TreeDecisionNode(
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field1FixedTo1 = new FixedField(field1, Stream.of(1), FieldSpec.Empty, nullMonitor);
        ReductiveState field1Fixed = initialState.with(field1FixedTo1);
        field1FixedTo1.getStream().iterator().next(); //move to the first value for field1, i.e. 1

        ReductiveConstraintNode reduced = reducer.reduce(tree, field1Fixed);

        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            hasItems(isGreaterThanOrEqualTo2, isLessThan2));
        Assert.assertThat(reduced.getDecisions().size(), equalTo(1));
    }

    @Test
    public void shouldRemoveDecisionsBelowARemovedConstraint(){
        AtomicConstraint field3IsGreaterThan4 = new IsGreaterThanConstantConstraint(field3, 4, Collections.emptySet());
        TreeConstraintNode field2GreaterThanOrEqualTo2Option = new TreeConstraintNode( //expect this to be removed
            Collections.singletonList(isGreaterThanOrEqualTo2),
            Collections.singletonList(
                new TreeDecisionNode(
                    new TreeConstraintNode(field3IsGreaterThan4) //something that isn't yet fixed and itself wouldn't be removed
                )
            ));
        DecisionNode decision1 = new TreeDecisionNode(
            field2GreaterThanOrEqualTo2Option, //field2 >= 2
            new TreeConstraintNode(isLessThan2) //field3
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field2FixedTo0 = new FixedField(field2, Stream.of(0), FieldSpec.Empty, nullMonitor);
        ReductiveState field2Fixed = initialState.with(field2FixedTo0);
        field2FixedTo0.getStream().iterator().next(); //move to the first value for field2, i.e. 0

        ReductiveConstraintNode reduced = reducer.reduce(tree, field2Fixed);

        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            not(hasItems(isGreaterThanOrEqualTo2, field3IsGreaterThan4)));
    }

    @Test
    public void shouldSimplifyTheTreeAfterReducing(){
        AtomicConstraint field3IsGreaterThan4 = new IsGreaterThanConstantConstraint(field3, 4, Collections.emptySet());
        TreeConstraintNode field2GreaterThanOrEqualTo2Option = new TreeConstraintNode( //expect this to be removed
            Collections.singletonList(isGreaterThanOrEqualTo2),
            Collections.singletonList(
                new TreeDecisionNode(
                    new TreeConstraintNode(field3IsGreaterThan4) //something that isn't yet fixed and itself wouldn't be removed
                )
            ));
        DecisionNode decision1 = new TreeDecisionNode(
            field2GreaterThanOrEqualTo2Option, //field2 >= 2
            new TreeConstraintNode(isLessThan2) //field3
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field2FixedTo0 = new FixedField(field2, Stream.of(0), FieldSpec.Empty, nullMonitor);
        ReductiveState field2Fixed = initialState.with(field2FixedTo0);
        field2FixedTo0.getStream().iterator().next(); //move to the first value for field2, i.e. 0

        ReductiveConstraintNode reduced = reducer.reduce(tree, field2Fixed);

        Assert.assertThat(
            reduced.getDecisions().size(),
            equalTo(0));
        Assert.assertThat(
            reduced.getAtomicConstraints(),
            containsInAnyOrder(isLessThan2, isEqualTo1)
        );
    }

    @Test
    public void shouldExposeAllAtomicConstraintsIfNoFieldsAreFixed(){
        DecisionNode decision1 = new TreeDecisionNode(
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);

        ReductiveConstraintNode reduced = reducer.reduce(tree, initialState);

        Assert.assertThat(
            reduced.getAllUnfixedAtomicConstraints(),
            containsInAnyOrder(isEqualTo1, isGreaterThanOrEqualTo2, isLessThan2));
    }

    @Test
    public void shouldExposeAListOfAllAtomicConstraintsThatAreNotFixed(){
        DecisionNode decision1 = new TreeDecisionNode(
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field1FixedTo1 = new FixedField(field1, Stream.of(1), FieldSpec.Empty, nullMonitor);
        ReductiveState field1Fixed = initialState.with(field1FixedTo1);
        field1FixedTo1.getStream().iterator().next(); //move to the first value for field1, i.e. 1

        ReductiveConstraintNode reduced = reducer.reduce(tree, field1Fixed);

        Assert.assertThat(
            reduced.getAllUnfixedAtomicConstraints(),
            containsInAnyOrder(isGreaterThanOrEqualTo2, isLessThan2));
    }

    @Test
    public void shouldReturnNullIfTreeReductionInvalidatesTheTree(){
        DecisionNode decision1 = new TreeDecisionNode( //should have every option removed, therefore invalidating the tree
            new TreeConstraintNode(isGreaterThanOrEqualTo2),
            new TreeConstraintNode(isLessThan2)
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field2FixedTo0 = new FixedField(field2, Stream.of(0), FieldSpec.Empty, nullMonitor);
        FixedField field3FixedTo4 = new FixedField(field3, Stream.of(4), FieldSpec.Empty, nullMonitor);
        ReductiveState fields2And3Fixed = initialState.with(field2FixedTo0).with(field3FixedTo4);
        field2FixedTo0.getStream().iterator().next(); //move to the first value for field2, i.e. 0
        field3FixedTo4.getStream().iterator().next(); //move to the first value for field3, i.e. 4

        ReductiveConstraintNode reduced = reducer.reduce(tree, fields2And3Fixed);

        Assert.assertThat(
            reduced,
            equalTo(null));
    }

    @Test
    public void shouldReturnNullIfALowLevelDecisionIsInvalidated(){
        AtomicConstraint field3IsGreaterThan4 = new IsGreaterThanConstantConstraint(field3, 4, Collections.emptySet());
        TreeConstraintNode field2GreaterThanOrEqualTo2Option = new TreeConstraintNode( //expect this to be removed
            Collections.singletonList(isGreaterThanOrEqualTo2),
            Collections.singletonList(
                new TreeDecisionNode(
                    new TreeConstraintNode(field3IsGreaterThan4) //something that isn't yet fixed and itself wouldn't be removed
                )
            ));
        DecisionNode decision1 = new TreeDecisionNode(
            field2GreaterThanOrEqualTo2Option, //field2 >= 2
            new TreeConstraintNode(isLessThan2) //field3
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field3FixedTo3 = new FixedField(field3, Stream.of(3), FieldSpec.Empty, nullMonitor);
        ReductiveState field3Fixed = initialState.with(field3FixedTo3);
        field3FixedTo3.getStream().iterator().next(); //move to the first value for field3, i.e. 3

        ReductiveConstraintNode reduced = reducer.reduce(tree, field3Fixed);

        Assert.assertThat(reduced, equalTo(null));
    }


    @Test
    public void shouldIncludeAtomicConstraintIfNotContradictoryWithNullForFixedField(){
        AtomicConstraint field3IsGreaterThan4 = new IsGreaterThanConstantConstraint(field3, 4, Collections.emptySet());
        TreeConstraintNode field2GreaterThanOrEqualTo2Option = new TreeConstraintNode(
            Collections.singletonList(isGreaterThanOrEqualTo2),
            Collections.singletonList(
                new TreeDecisionNode(
                    new TreeConstraintNode(field3IsGreaterThan4) //something that isn't yet fixed and itself wouldn't be removed
                )
            ));
        DecisionNode decision1 = new TreeDecisionNode(
            field2GreaterThanOrEqualTo2Option, //field2 >= 2
            new TreeConstraintNode(isLessThan2) //field3
        );
        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isEqualTo1),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field3FixedTo3 = new FixedField(field3, Collections.singletonList(null).stream(), FieldSpec.Empty, nullMonitor);
        ReductiveState field3Fixed = initialState.with(field3FixedTo3);
        field3FixedTo3.getStream().iterator().next(); //move to the first value for field3, i.e. null

        ReductiveConstraintNode reduced = reducer.reduce(tree, field3Fixed);

        //fixed value of field3 of null does not contradict any constraints so we get back everything

        Assert.assertThat(reduced.getAtomicConstraints(), containsInAnyOrder(isEqualTo1));
        Assert.assertThat(reduced.getDecisions().size(), equalTo(1));
        Assert.assertThat(
            AtomicConstraintExtractor.getAllAtomicConstraintsRecursively(reduced),
            containsInAnyOrder(isEqualTo1, isGreaterThanOrEqualTo2, isLessThan2, field3IsGreaterThan4));
    }




    @Test
    public void shouldReturnNullIfTreeReductionWithFixedValueNotNullInvalidatesTheTree(){
        AtomicConstraint field3IsGreaterThan4 = new IsGreaterThanConstantConstraint(field3, 4, Collections.emptySet());
        TreeConstraintNode field2GreaterThanOrEqualTo2Option = new TreeConstraintNode(
            Collections.singletonList(isGreaterThanOrEqualTo2),
            Collections.singletonList(
                new TreeDecisionNode(
                    new TreeConstraintNode(field3IsGreaterThan4) //something that isn't yet fixed and itself wouldn't be removed
                )
            ));
        DecisionNode decision1 = new TreeDecisionNode(
            field2GreaterThanOrEqualTo2Option, //field2 >= 2
            new TreeConstraintNode(isLessThan2) //field3
        );


        IsNullConstraint isNull = new IsNullConstraint(field3, Collections.emptySet());
        AtomicConstraint isNotNull = isNull.negate();

        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isNotNull),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field3FixedTo3 = new FixedField(field3, Stream.of(3), FieldSpec.Empty, nullMonitor);
        ReductiveState field3Fixed = initialState.with(field3FixedTo3);
        field3FixedTo3.getStream().iterator().next(); //move to the first value for field3, i.e. null

        ReductiveConstraintNode reduced = reducer.reduce(tree, field3Fixed);

        //fixed value of field3 of null contradicts the constraint field3 must not be null

        Assert.assertThat(reduced, equalTo(null));
    }



    @Test
    public void shouldReturnNullIfTreeReductionWithFixedValueNullInvalidatesTheTree(){
        AtomicConstraint field3IsGreaterThan4 = new IsGreaterThanConstantConstraint(field3, 4, Collections.emptySet());
        TreeConstraintNode field2GreaterThanOrEqualTo2Option = new TreeConstraintNode(
            Collections.singletonList(isGreaterThanOrEqualTo2),
            Collections.singletonList(
                new TreeDecisionNode(
                    new TreeConstraintNode(field3IsGreaterThan4) //something that isn't yet fixed and itself wouldn't be removed
                )
            ));
        DecisionNode decision1 = new TreeDecisionNode(
            field2GreaterThanOrEqualTo2Option, //field2 >= 2
            new TreeConstraintNode(isLessThan2) //field3
        );

        IsNullConstraint isNull = new IsNullConstraint(field3, Collections.emptySet());
        AtomicConstraint isNotNull = isNull.negate();


        ConstraintNode tree = new TreeConstraintNode(
            Collections.singletonList(isNotNull),
            Collections.singletonList(decision1));
        ReductiveDecisionTreeReducer reducer = new ReductiveDecisionTreeReducer(
            fieldSpecFactory,
            fieldSpecMerger,
            simplifier);
        FixedField field3FixedTo3 = new FixedField(field3, Collections.singletonList(null).stream(), FieldSpec.Empty, nullMonitor);
        ReductiveState field3Fixed = initialState.with(field3FixedTo3);
        field3FixedTo3.getStream().iterator().next(); //move to the first value for field3, i.e. null

        ReductiveConstraintNode reduced = reducer.reduce(tree, field3Fixed);

        //fixed value of field3 of null contradicts the constraint field3 must not be null

        Assert.assertThat(reduced, equalTo(null));
    }



    private static class AtomicConstraintExtractor extends BaseVisitor{
        public ArrayList<AtomicConstraint> atomicConstraints = new ArrayList<>();

        @Override
        public AtomicConstraint visit(AtomicConstraint atomicConstraint) {
            atomicConstraints.add(atomicConstraint);
            return atomicConstraint;
        }

        public static Collection<AtomicConstraint> getAllAtomicConstraintsRecursively(ConstraintNode node){
            AtomicConstraintExtractor extractor = new AtomicConstraintExtractor();
            node.accept(extractor);
            return extractor.atomicConstraints;
        }
    }
}
