package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.NotConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldCollection {
    private final GenerationConfig generationConfig;
    private final ConstraintReducer reducer;
    private final FieldCollectionFactory fieldCollectionFactory;
    private final FieldSpecMerger fieldSpecMerger;
    private final FieldSpecFactory fieldSpecFactory;
    private final ProfileFields fields;
    private final Map<Field, FixedField> fixedFields;
    private final FixedField lastFixedField;
    private final FixFieldStrategy fixFieldStrategy;

    FieldCollection(
        ProfileFields fields,
        FieldCollectionFactory fieldCollectionFactory,
        GenerationConfig config,
        ConstraintReducer constraintReducer,
        FieldSpecMerger fieldSpecMerger,
        FieldSpecFactory fieldSpecFactory,
        FixFieldStrategy fixFieldStrategy,
        Map<Field, FixedField> fixedFields,
        FixedField lastFixedField) {
        this.fields = fields;
        this.fieldCollectionFactory = fieldCollectionFactory;
        this.fieldSpecMerger = fieldSpecMerger;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fixFieldStrategy = fixFieldStrategy;
        this.fixedFields = fixedFields;
        this.lastFixedField = lastFixedField;
        this.generationConfig = config;
        this.reducer = constraintReducer;
    }

    public boolean allFieldsAreFixed() {
        int noOfFixedFields = this.lastFixedField == null
            ? this.fixedFields.size()
            : this.fixedFields.size() + 1;

        return noOfFixedFields == this.fields.size();
    }

    //get a stream of all possible values for the field that was fixed on the last iteration
    public Stream<Object> getValuesFromLastFixedField(){
        if (this.lastFixedField == null)
            throw new NullPointerException("Field has not been fixed yet");

        return this.lastFixedField.getStream();
    }

    //produce a stream of RowSpecs for each value in the permitted set of values for the field fixed on the last iteration
    public Stream<RowSpec> createRowSpecFromFixedValues(ConstraintNode constraintNode) {
        //create a row spec where every field is set to this.fixedFields & field=value
        if (this.lastFixedField == null) {
            throw new UnsupportedOperationException("Field has not been fixed yet");
        }

        Map<Field, FieldSpec> fieldSpecsPerField = getFieldSpecsForAllFixedFieldsExceptLast(constraintNode);

        if (fieldSpecsPerField.values().stream().anyMatch(fieldSpec -> fieldSpec == FieldSpec.Empty)){
            return Stream.empty();
        }


        FieldSpec fieldSpecForValuesInLastFixedField = this.lastFixedField.getFieldSpecForValues();
        fieldSpecsPerField.put(this.lastFixedField.field, fieldSpecForValuesInLastFixedField);

        RowSpec rowSpecWithAllValuesForLastFixedField = new ReductiveRowSpec(
            this.fields,
            fieldSpecsPerField,
            this.lastFixedField.field
        );

        System.out.println(String.format(
            "%s %s",
            this.lastFixedField.field.name,
            fieldSpecForValuesInLastFixedField.toString()));
        return Stream.of(rowSpecWithAllValuesForLastFixedField);
    }

    //work out the next field to fix and return a new FieldCollection with this field fixed
    public FieldCollection getNextFixedField(ReductiveConstraintNode rootNode) {
        FieldAndConstraintMapping fieldToFix = this.fixFieldStrategy.getFieldAndConstraintMapToFixNext(rootNode);

        if (fieldToFix == null){
            throw new UnsupportedOperationException(
                String.format(
                    "Unable to find a field to fix, no finite constraints\nUnfixed fields: %s",
                    Objects.toString(this.getUnfixedFields())));
        }

        FixedField field = getFixedFieldWithValuesForField(fieldToFix.getField(), rootNode);
        return this.fieldCollectionFactory.create(this, field);
    }

    //for the given field get a stream of possible values
    private FixedField getFixedFieldWithValuesForField(Field field, ConstraintNode rootNode) {
        //from the original tree, get all atomic constraints that match the given field
        Set<AtomicConstraint> constraintsForRootNode = rootNode.getAtomicConstraints()
            .stream()
            .filter(c -> c.getField().equals(field))
            .collect(Collectors.toSet());
        Set<AtomicConstraint> constraintsForDecisions = rootNode.getDecisions()
            .stream()
            .flatMap(d -> d.getOptions().stream())
            .flatMap(o -> o.getAtomicConstraints().stream())
            .filter(c -> !(c instanceof NotConstraint) && c.getField().equals(field))
            .collect(Collectors.toSet());

        //produce a fieldspec for all the atomic constraints
        FieldSpec rootConstraintsFieldSpec = this.reducer.reduceConstraintsToFieldSpec(
            constraintsForRootNode, constraintsForDecisions)
            .orElse(FieldSpec.Empty);

        //use the FieldSpecFulfiller to emit all possible values given the generation mode, interesting or full-sequential
        Stream<Object> values = new FieldSpecFulfiller(field, rootConstraintsFieldSpec)
            .generate(this.generationConfig)
            .map(dataBag -> dataBag.getValue(field));

        return new FixedField(field, values, rootConstraintsFieldSpec);
    }

    //Given the current set of fixed fields, work out if the given atomic constraint is contradictory, whether the field is fixed or not
    AtomicConstraintFixedFieldBehaviour shouldIncludeAtomicConstraint(AtomicConstraint atomicConstraint) {
        //is the field for this atomic constraint fixed?
        //does the constraint complement or conflict with the fixed field?

        Field field = atomicConstraint.getField();
        FixedField fixedFieldValue = getFixedField(field);
        if (fixedFieldValue == null){
            //field isn't fixed
            return AtomicConstraintFixedFieldBehaviour.FIELD_NOT_FIXED;
        }

        //field is fixed, work out if it is contradictory
        return fixedValueConflictsWithAtomicConstraint(fixedFieldValue, atomicConstraint)
            ? AtomicConstraintFixedFieldBehaviour.CONSTRAINT_CONTRADICTS
            : AtomicConstraintFixedFieldBehaviour.NON_CONTRADICTORY;
    }

    //work out if the field is contradictory
    private boolean fixedValueConflictsWithAtomicConstraint(FixedField fixedField, AtomicConstraint atomicConstraint) {
        FieldSpec fieldSpec = fieldSpecFactory.construct(atomicConstraint);
        FieldSpec fixedValueFieldSpec = fixedField.getFieldSpecForCurrentValue();

        Optional<FieldSpec> merged = fieldSpecMerger.merge(fixedValueFieldSpec, fieldSpec);
        return !merged.isPresent(); //no conflicts
    }

    //get a copy of the current fixed field for the given field, will return null if the field isn't fixed
    private FixedField getFixedField(Field field) {
        if (lastFixedField != null && lastFixedField.field.equals(field)){
            return lastFixedField;
        }

        return this.fixedFields.getOrDefault(field, null);
    }

    //create a mapping of field->fieldspec for each fixed field - efficiency
    private Map<Field, FieldSpec> getFieldSpecsForAllFixedFieldsExceptLast(ConstraintNode constraintNode){
        Map<Field, List<AtomicConstraint>> fieldToConstraints = constraintNode.getAtomicConstraints()
            .stream()
            .collect(Collectors.groupingBy(AtomicConstraint::getField));

        return this.fixedFields.values()
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

    //create a FieldSpec for a given FixedField and the atomic constraints we know about this field
    private FieldSpec getFieldSpec(FixedField fixedField, Collection<AtomicConstraint> constraintsForField) {
        FieldSpec fixedFieldSpec = fixedField.getFieldSpecForCurrentValue();
        Optional<FieldSpec> constrainedFieldSpecOpt = this.reducer.reduceConstraintsToFieldSpec(constraintsForField);

        if (!constrainedFieldSpecOpt.isPresent()){
            return null; //this shouldn't happen: caused by constraints for one of the fixed fields contradicting each other (issue in optimising and/or reducing) - see issue #250
        }

        return this.fieldSpecMerger
            .merge(fixedFieldSpec, constrainedFieldSpecOpt.get())
            .orElseThrow(() -> new UnsupportedOperationException("Contradiction? - " + fixedField.toString() + "\n" + this.toString(true)));
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public Map<Field, FixedField> getFixedFields(){
        return this.fixedFields;
    }

    Map.Entry<Field, FixedField> getLastFixedField(){
        if (this.lastFixedField == null){
            return null;
        }

        return new HashMap.SimpleEntry<>(this.lastFixedField.field, this.lastFixedField);
    }

    private Set<Field> getUnfixedFields(){
        return this.fields.stream()
            .filter(f -> !this.fixedFields.containsKey(f) && !f.equals(this.lastFixedField.field))
            .collect(Collectors.toSet());
    }

    public String toString(boolean detailAllFields) {
        String fixedFieldsString = this.fixedFields.size() > 10 && !detailAllFields
            ? String.format("Fixed fields: %d of %d", this.fixedFields.size(), this.fields.size())
            : String.join(", ", this.fixedFields.values()
                .stream()
                .sorted(Comparator.comparing(ff -> ff.field.name))
                .map(FixedField::toString)
                .collect(Collectors.toList()));

        if (this.lastFixedField == null) {
            return fixedFieldsString;
        }

        return this.fixedFields.isEmpty()
            ? this.lastFixedField.toString()
            : this.lastFixedField.toString() + " & " + fixedFieldsString;
    }

    public ProfileFields getFields() {
        return this.fields;
    }
}
