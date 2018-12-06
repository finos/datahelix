package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ReductiveDecisionTreeAdapter {
    private final DecisionTreeSimplifier simplifier = new DecisionTreeSimplifier();

    public ReductiveConstraintNode adapt(ConstraintNode rootNode, FieldCollection fixedFields){
        AdapterContext context = new AdapterContext();
        ConstraintNode node = adapt(rootNode, fixedFields, context);

        if (!context.isValid() || node == null){
            return null;
        }

        return new ReductiveConstraintNode(
            this.simplifier.simplify(node),
            context.getAllUnfixedAtomicConstraints());
    }

    private ConstraintNode adapt(ConstraintNode rootNode, FieldCollection fixedFields, AdapterContext context){
        ConstraintNode node = new TreeConstraintNode(
            context.isValid() ? getAtomicConstraints(rootNode, fixedFields, context) : Collections.emptySet(),
            context.isValid() ? getDecisions(rootNode, fixedFields, context) : Collections.emptySet()
        );

        return context.isValid()
            ? this.simplifier.simplify(node)
            : null;
    }

    private DecisionNode adapt(DecisionNode decision, FieldCollection fixedFields, AdapterContext context){
        return new TreeDecisionNode(decision.getOptions()
            .stream()
            .map(o -> adapt(o, fixedFields, context.forOption(o)))
            .filter(o -> o != null && !o.getAtomicConstraints().isEmpty())
            .collect(Collectors.toList()));
    }

    private static Collection<IConstraint> getAtomicConstraints(ConstraintNode constraint, FieldCollection fixedFields, AdapterContext context) {
        Collection<IConstraint> potentialResult = constraint
            .getAtomicConstraints().stream()
            .filter(atomicConstraint -> {
                AtomicConstraintFixedFieldBehaviour behaviour = fixedFields.shouldIncludeAtomicConstraint(atomicConstraint);
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

    private Collection<DecisionNode> getDecisions(ConstraintNode constraint, FieldCollection fixedFields, AdapterContext context) {
        return constraint.getDecisions()
            .stream()
            .map(d -> adapt(d, fixedFields, context))
            .filter(d -> !d.getOptions().isEmpty())
            .collect(Collectors.toList());
    }
}

