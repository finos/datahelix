package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.Nullness;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReductiveDecisionTreeReducer {

    private final DecisionTreeSimplifier simplifier;
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;

    public ReductiveDecisionTreeReducer(
        FieldSpecFactory fieldSpecFactory,
        FieldSpecMerger fieldSpecMerger,
        DecisionTreeSimplifier simplifier){
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
        this.simplifier = simplifier;
    }

    public ReductiveConstraintNode reduce(ConstraintNode rootNode, ReductiveState fixedFields){
        AdapterContext context = new AdapterContext();
        ConstraintNode node = reduce(rootNode, fixedFields, context);

        if (!context.isValid() || node == null){
            return null;
        }

        return new ReductiveConstraintNode(
            this.simplifier.simplify(node),
            context.getAllUnfixedAtomicConstraints());
    }

    private ConstraintNode reduce(ConstraintNode rootNode, ReductiveState fixedFields, AdapterContext context){
        ConstraintNode node = new TreeConstraintNode(
            context.isValid() ? getAtomicConstraints(rootNode, fixedFields, context) : Collections.emptySet(),
            context.isValid() ? getDecisions(rootNode, fixedFields, context) : Collections.emptySet()
        );

        return context.isValid()
            ? this.simplifier.simplify(node)
            : null;
    }

    private DecisionNode reduce(DecisionNode decision, ReductiveState fixedFields, AdapterContext context){
        TreeDecisionNode reducedDecision = new TreeDecisionNode(decision.getOptions()
            .stream()
            .map(o -> reduce(o, fixedFields, context.forOption(o)))
            .filter(o -> o != null && !o.getAtomicConstraints().isEmpty())
            .collect(Collectors.toList()));

        if (reducedDecision.getOptions().isEmpty()){
            //NOTE: Presumes the tree is 'valid' to start off with, i.e. all decision nodes have at least 1 option
            context.treeIsInvalid();
        }

        return reducedDecision;
    }

    private Collection<AtomicConstraint> getAtomicConstraints(ConstraintNode constraint, ReductiveState fixedFields, AdapterContext context) {
        Collection<AtomicConstraint> potentialResult = constraint
            .getAtomicConstraints().stream()
            .filter(atomicConstraint -> {
                AtomicConstraintFixedFieldBehaviour behaviour = shouldIncludeAtomicConstraint(fixedFields, atomicConstraint);
                switch (behaviour) {
                    case NON_CONTRADICTORY:
                        context.addNonContradictoryAtomicConstraint(atomicConstraint);
                        return true;
                    case FIELD_NOT_FIXED:
                        context.addUnfixedAtomicConstraint(atomicConstraint);
                        return true;
                    case CONSTRAINT_CONTRADICTS:
                        context.addConflictingAtomicConstraint(atomicConstraint);
                        context.setIsInvalid();
                        return false;
                }

                context.setIsInvalid();
                return false;
            })
            .collect(Collectors.toSet());

        return context.isValid()
            ? potentialResult
            : Collections.emptySet();
    }

    private Collection<DecisionNode> getDecisions(ConstraintNode constraint, ReductiveState fixedFields, AdapterContext context) {
        return constraint.getDecisions()
            .stream()
            .map(d -> reduce(d, fixedFields, context))
            .filter(d -> !d.getOptions().isEmpty())
            .collect(Collectors.toList());
    }


    //Given the current set of fixed fields, work out if the given atomic constraint is contradictory, whether the field is fixed or not
    private AtomicConstraintFixedFieldBehaviour shouldIncludeAtomicConstraint(ReductiveState fixedFields, AtomicConstraint atomicConstraint) {
        //is the field for this atomic constraint fixed?
        //does the constraint complement or conflict with the fixed field?

        Field field = atomicConstraint.getField();
        FixedField fixedFieldValue = fixedFields.getFixedField(field);
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

        if (!merged.isPresent()) {
            return true;
        }

        FieldSpec mergedFieldSpec = merged.get();

        if (mergedFieldSpec.getSetRestrictions() == null) {
            return false;
        }

        Set<Object> mergedFieldSpecWhitelist = mergedFieldSpec.getSetRestrictions().getWhitelist();

        if (mergedFieldSpecWhitelist == null || !mergedFieldSpecWhitelist.contains(fixedField.getCurrentValue())) {
            if(mergedFieldSpec.getNullRestrictions() == null){
                return false; // the fixedField is not in the whitelist but there are no null restrictions so it can be null
            }

            if((mergedFieldSpec.getNullRestrictions() != null && mergedFieldSpec.getNullRestrictions().nullness == Nullness.MUST_BE_NULL)){
                return false; // the fixedField is not in the whitelist but can be null
            }

            return true; //the fixedField value has been removed from the whitelist and the field cannot be null
        }

        return false;
    }

}

