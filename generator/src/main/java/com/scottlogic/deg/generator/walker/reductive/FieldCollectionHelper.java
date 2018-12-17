package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldCollectionHelper {

    private final GenerationConfig generationConfig;
    private final ConstraintReducer reducer;
    private final FieldSpecMerger fieldSpecMerger;
    private final FieldSpecFactory fieldSpecFactory;
    private final FixFieldStrategy fixFieldStrategy;
    private final ReductiveDataGeneratorMonitor monitor;

    public FieldCollectionHelper(
        GenerationConfig config,
        ConstraintReducer constraintReducer,
        FieldSpecMerger fieldSpecMerger,
        FieldSpecFactory fieldSpecFactory,
        FixFieldStrategy fixFieldStrategy,
        ReductiveDataGeneratorMonitor monitor) {
        this.fieldSpecMerger = fieldSpecMerger;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fixFieldStrategy = fixFieldStrategy;
        this.generationConfig = config;
        this.reducer = constraintReducer;
        this.monitor = monitor;
    }


    //produce a stream of RowSpecs for each value in the permitted set of values for the field fixed on the last iteration
    public Stream<RowSpec> createRowSpecFromFixedValues(FieldCollection fieldCollection, ConstraintNode constraintNode) {
        //create a row spec where every field is set to this.fixedFields & field=value
        if (fieldCollection.getLastFixedField() == null) {
            throw new UnsupportedOperationException("Field has not been fixed yet");
        }

        Map<Field, FieldSpec> fieldSpecsPerField = getFieldSpecsForAllFixedFieldsExceptLast(fieldCollection, constraintNode);

        if (fieldSpecsPerField.values().stream().anyMatch(fieldSpec -> fieldSpec == FieldSpec.Empty)){
            return Stream.empty();
        }

        FieldSpec fieldSpecForValuesInLastFixedField = fieldCollection.getLastFixedField().getFieldSpecForValues();
        fieldSpecsPerField.put(fieldCollection.getLastFixedField().field, fieldSpecForValuesInLastFixedField);

        RowSpec rowSpecWithAllValuesForLastFixedField = new ReductiveRowSpec(
            fieldCollection.getFields(),
            fieldSpecsPerField,
            fieldCollection.getLastFixedField().field
        );

        this.monitor.rowSpecEmitted(
            fieldCollection.getLastFixedField(),
            fieldSpecForValuesInLastFixedField,
            rowSpecWithAllValuesForLastFixedField);
        return Stream.of(rowSpecWithAllValuesForLastFixedField);
    }

    //work out the next field to fix and return a new FieldCollection with this field fixed
    public FieldCollection getNextFixedField(FieldCollection fieldCollection, ReductiveConstraintNode rootNode) {
        Field fieldToFix = this.fixFieldStrategy.getNextFieldToFix(fieldCollection, rootNode);

        if (fieldToFix == null){
            throw new UnsupportedOperationException(
                String.format(
                    "Unable to find a field to fix, no finite constraints\nUnfixed fields: %s",
                    Objects.toString(fieldCollection.getUnfixedFields())));
        }

        FixedField field = getFixedFieldWithValuesForField(fieldToFix, rootNode);
        return fieldCollection.with(field);
    }


    //Given the current set of fixed fields, work out if the given atomic constraint is contradictory, whether the field is fixed or not
    AtomicConstraintFixedFieldBehaviour shouldIncludeAtomicConstraint(FieldCollection fieldCollection, AtomicConstraint atomicConstraint) {
        //is the field for this atomic constraint fixed?
        //does the constraint complement or conflict with the fixed field?

        Field field = atomicConstraint.getField();
        FixedField fixedFieldValue = fieldCollection.getFixedField(field);
        if (fixedFieldValue == null){
            //field isn't fixed
            return AtomicConstraintFixedFieldBehaviour.FIELD_NOT_FIXED;
        }

        //field is fixed, work out if it is contradictory
        return fixedValueConflictsWithAtomicConstraint(fixedFieldValue, atomicConstraint)
            ? AtomicConstraintFixedFieldBehaviour.CONSTRAINT_CONTRADICTS
            : AtomicConstraintFixedFieldBehaviour.NON_CONTRADICTORY;
    }

    //create a mapping of field->fieldspec for each fixed field - efficiency
    private Map<Field, FieldSpec> getFieldSpecsForAllFixedFieldsExceptLast(FieldCollection fieldCollection, ConstraintNode constraintNode){
        Map<Field, List<AtomicConstraint>> fieldToConstraints = constraintNode.getAtomicConstraints()
            .stream()
            .collect(Collectors.groupingBy(AtomicConstraint::getField));

        return fieldCollection.getFixedFields().values()
            .stream()
            .collect(Collectors.toMap(
                ff -> ff.field,
                ff -> {
                    FieldSpec fieldSpec = getFieldSpec(ff, fieldToConstraints.get(ff.field));
                    return fieldSpec == null
                        ? FieldSpec.Empty
                        : fieldSpec;
                }
            ));
    }


    //TODO move this, as it is all fixedField related, and not field collection stuff
    //for the given field get a stream of possible values
    private FixedField getFixedFieldWithValuesForField(Field field, ConstraintNode rootNode) {
        //from the original tree, get all atomic constraints that match the given field
        Set<AtomicConstraint> constraintsForRootNode = rootNode.getAtomicConstraints()
            .stream()
            .filter(c -> c.getField().equals(field))
            .collect(Collectors.toSet());

        //produce a fieldspec for all the atomic constraints
        FieldSpec rootConstraintsFieldSpec = this.reducer.reduceConstraintsToFieldSpec(constraintsForRootNode)
            .orElse(FieldSpec.Empty);

        //use the FieldSpecFulfiller to emit all possible values given the generation mode, interesting or full-sequential
        Stream<Object> values = new FieldSpecFulfiller(field, rootConstraintsFieldSpec)
            .generate(this.generationConfig)
            .map(dataBag -> dataBag.getValue(field));

        return new FixedField(field, values, rootConstraintsFieldSpec, this.monitor);
    }
    //work out if the field is contradictory
    private boolean fixedValueConflictsWithAtomicConstraint(FixedField fixedField, AtomicConstraint atomicConstraint) {
        FieldSpec fieldSpec = fieldSpecFactory.construct(atomicConstraint);
        FieldSpec fixedValueFieldSpec = fixedField.getFieldSpecForCurrentValue();

        Optional<FieldSpec> merged = fieldSpecMerger.merge(fixedValueFieldSpec, fieldSpec);
        return !merged.isPresent(); //no conflicts
    }

    //create a FieldSpec for a given FixedField and the atomic constraints we know about this field
    private FieldSpec getFieldSpec(FixedField fixedField, Collection<AtomicConstraint> constraintsForField) {
        FieldSpec fixedFieldSpec = fixedField.getFieldSpecForCurrentValue();
        Optional<FieldSpec> constrainedFieldSpecOpt = this.reducer.reduceConstraintsToFieldSpec(constraintsForField);

        if (!constrainedFieldSpecOpt.isPresent()){
            return null; //this shouldn't happen: caused by constraints for one of the fixed fields contradicting each other (issue in optimising and/or reducing) - see issue #250
        }

        return this.fieldSpecMerger
            .merge(fixedFieldSpec, constrainedFieldSpecOpt.get())
            .orElseThrow(() -> new UnsupportedOperationException("Contradiction? - " + fixedField.toString() + "\n" + constraintsForField.toString()));
    }

}
