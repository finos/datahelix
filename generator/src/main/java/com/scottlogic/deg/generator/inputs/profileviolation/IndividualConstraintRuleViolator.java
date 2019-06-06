package com.scottlogic.deg.generator.inputs.profileviolation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
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
        List<Constraint> newConstraints = new ArrayList<>();
        List<Constraint> violate = new ArrayList<>();
        for (Constraint constraint:rule.constraints) {
            if (canViolate(constraint)){
                violate.add(constraint);
            } else {
                newConstraints.add(constraint);
            }
        }

        if (!violate.isEmpty()) {
            newConstraints.add(
                violateConstraint(
                    violate.size() == 1
                    ? violate.get(0)
                    : new AndConstraint(violate)
                ));
        }
        return new Rule(rule.ruleInformation, newConstraints);
    }

    /**
     * Violates a given input constraint according to negation rules.
     * @param constraint Constraint to violate
     * @return Constraint with violated logic
     */
    private Constraint violateConstraint(Constraint constraint) {
        return constraint.negate();
    }

    /**
     * Checks that the constraint can be violated given all of the Violation Filters.
     * @param constraint Constraint to check.
     * @return True if constraint van be violated, otherwise false.
     */
    private boolean canViolate(Constraint constraint){
        for (ViolationFilter filter : constraintsToNotViolate) {
            if (!filter.canViolate(constraint)){
                return false;
            }
        }
        return true;
    }
}