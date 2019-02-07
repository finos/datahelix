package com.scottlogic.deg.generator.inputs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule violator which violates rules by individually violating each constraint within the rule.
 * I.E. VIOLATE(A,B,C) => VIOLATE(A),B,C OR A,VIOLATE(B),C OR A,B,VIOLATE(C)
 */
public class IndividualConstraintRuleViolator implements RuleViolator {
    private final List<ViolationFilter> constraintsToNotViolate;

    @Inject
    public IndividualConstraintRuleViolator(List<ViolationFilter> constraintsToNotViolate) {
        this.constraintsToNotViolate = constraintsToNotViolate;
    }

    @Override
    public Rule violateRule(Rule rule) {
        List<Constraint> violate = new ArrayList<>();
        List<Constraint> unviolated = new ArrayList<>();
        for (Constraint constraint:rule.constraints) {
            if (acceptConstraint(constraint)){
                violate.add(constraint);
            } else {
                unviolated.add(constraint);
            }
        }
        if (violate.isEmpty()) {
            return rule;
        }

        // TODO: REMOVE The ViolateConstraint wraps the violated constraints.
        // It is used in the tree factory to create the violated tree
        ViolateConstraint violatedConstraint = new ViolateConstraint(
            violate.size() == 1
                ? violate.iterator().next()
                : new AndConstraint(violate));

        unviolated.add(violatedConstraint);
        return new Rule(rule.ruleInformation, unviolated);
    }

    private boolean acceptConstraint(Constraint constraint){
        for (ViolationFilter filter : constraintsToNotViolate) {
            if (!filter.accept(constraint)){
                return false;
            }
        }
        return true;
    }
}