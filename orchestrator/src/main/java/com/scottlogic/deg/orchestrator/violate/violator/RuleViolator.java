/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.violate.violator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.NegatedGrammaticalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraintdetail.UnviolatableConstraintException;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rule violator which violates rules by individually violating each constraint within the rule.
 * I.E. VIOLATE(A,B,C) => VIOLATE(A),B,C OR A,VIOLATE(B),C OR A,B,VIOLATE(C)
 */
public class RuleViolator {
    private final List<ViolationFilter> constraintsToNotViolate;

    @Inject
    public RuleViolator(List<ViolationFilter> constraintsToNotViolate) {
        this.constraintsToNotViolate = constraintsToNotViolate;
    }

    public Rule violateRule(Rule rule) {
        List<Constraint> newConstraints = new ArrayList<>();
        List<Constraint> violate = new ArrayList<>();
        for (Constraint constraint:rule.getConstraints()) {
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
        return new Rule(rule.getRuleInformation(), newConstraints);
    }

    /**
     * Violates a given input constraint according to negation rules.
     * @param constraint Constraint to violate
     * @return Constraint with violated logic
     */
    private Constraint violateConstraint(Constraint constraint) {
        // VIOLATE(AND(X, Y, Z)) reduces to
        //   OR(
        //     AND(VIOLATE(X), Y, Z),
        //     AND(X, VIOLATE(Y), Z),
        //     AND(X, Y, VIOLATE(Z)))
        if (constraint instanceof AndConstraint) {
            Collection<Constraint> subConstraints = ((AndConstraint) constraint).getSubConstraints();

            Collection<Constraint> violatedIndividually =
                subConstraints.stream()
                    // for each subconstraint X, make a copy of the original list but with X replaced by VIOLATE(X)
                    .map(constraintToViolate ->
                        subConstraints.stream()
                            .map(c -> c == constraintToViolate
                                ? violateConstraint(c)
                                : c)
                            .collect(Collectors.toList()))
                    // make an AndConstraint out of each of the new lists
                    .map(AndConstraint::new)
                    .collect(Collectors.toList());

            return new OrConstraint(violatedIndividually);
        }
        // VIOLATE(OR(X, Y, Z)) reduces to AND(VIOLATE(X), VIOLATE(Y), VIOLATE(Z))
        else if (constraint instanceof OrConstraint) {
            Collection<Constraint> subConstraints = ((OrConstraint) constraint).subConstraints;

            Collection<Constraint> violatedAll =
            subConstraints.stream()
                .map(this::violateConstraint)
                .collect(Collectors.toList());

            return new AndConstraint(violatedAll);
        }
        // VIOLATE(IF(X, then: Y)) reduces to AND(X, VIOLATE(Y))
        // VIOLATE(IF(X, then: Y, else: Z)) reduces to OR(AND(X, VIOLATE(Y)), AND(VIOLATE(X), VIOLATE(Z)))
        else if (constraint instanceof ConditionalConstraint) {
            ConditionalConstraint conditional = ((ConditionalConstraint) constraint);

            Constraint positiveViolation = new AndConstraint(
                conditional.condition,
                violateConstraint(conditional.whenConditionIsTrue));

            Constraint negativeViolation = conditional.whenConditionIsFalse == null
                ? null
                : new AndConstraint(
                violateConstraint(conditional.condition),
                violateConstraint(conditional.whenConditionIsFalse));

            return  negativeViolation != null
                    ? new OrConstraint(positiveViolation, negativeViolation)
                    : positiveViolation;
        }
        // VIOLATE(NOT(AND(X,Y))) reduces to AND(X,Y)
        // VIOLATE(NOT(OR(X,Y))) reduces to OR(X,Y)
        // VIOLATE(NOT(IF(X,Y))) reduces to IF(X,Y)
        // VIOLATE(NOT(IF(X,Y,Z))) reduces to IF(X,Y,Z)
        else if (constraint instanceof NegatedGrammaticalConstraint) {
            return ((NegatedGrammaticalConstraint)constraint).getNegatedConstraint();
        }
        // VIOLATE(AtomicConstraint) reduces to NEGATE(AtomicConstraint)
        // We wrap this in a ViolatedAtomicConstraint to allow Visualise to show which constraint is being violated
        else if (constraint instanceof AtomicConstraint) {
            if (canViolate(constraint)) {
                return new ViolatedAtomicConstraint((AtomicConstraint)constraint.negate());
            }
            return constraint;
        }
        else {
            throw new UnviolatableConstraintException("Unable to find violation logic for specified constraint type: "
                + constraint.getClass());
        }
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