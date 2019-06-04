package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.NegatedGrammaticalConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.OrConstraint;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProfileDecisionTreeFactory implements DecisionTreeFactory {
    private final DecisionTreeSimplifier decisionTreeSimplifier = new DecisionTreeSimplifier();

    private static Collection<Constraint> wrapEach(
        Collection<Constraint> constraints,
        Function<Constraint, Constraint> wrapFunc) {

        return constraints.stream()
            .map(wrapFunc)
            .collect(Collectors.toList());
    }

    private static Collection<Constraint> negateEach(Collection<Constraint> constraints) {
        return wrapEach(constraints, Constraint::negate);
    }

    private static Constraint reduceConditionalConstraint(ConditionalConstraint constraint) {
        Constraint ifConstraint = constraint.condition;
        Constraint thenConstraint = constraint.whenConditionIsTrue;
        Constraint elseConstraint = constraint.whenConditionIsFalse;

        return new OrConstraint(
            ifConstraint.and(thenConstraint),
            elseConstraint != null
                ? ifConstraint.negate().and(elseConstraint)
                : ifConstraint.negate());
    }

    private static Collection<ConstraintNode> asConstraintNodeList(Collection<AtomicConstraint> constraints) {

        return Collections.singleton(
            new TreeConstraintNode(
                constraints,
                Collections.emptyList(),
                constraints.stream().anyMatch(ac -> ac instanceof ViolatedAtomicConstraint)
                    ? Collections.singleton(NodeMarking.VIOLATED)
                    : Collections.emptySet()
                ));
    }

    private static Collection<ConstraintNode> asConstraintNodeList(AtomicConstraint constraint) {

        return asConstraintNodeList(Collections.singleton(constraint));
    }

    private static Collection<ConstraintNode> asConstraintNodeList(DecisionNode decision) {
        return Collections.singleton(
            new TreeConstraintNode(
                Collections.emptyList(),
                Collections.singleton(decision)));
    }

    @Override
    public DecisionTree analyse(Profile profile) {
        return new DecisionTreeCollection(
            profile.getFields(),
            profile.getRules().stream()
                .map(rule -> new DecisionTree(convertRule(rule), profile.getFields(), profile.getDescription()))
                .map(decisionTreeSimplifier::simplify)
                .collect(Collectors.toList()))
            .getMergedTree();
    }

    private ConstraintNode convertRule(Rule rule) {
        Iterator<ConstraintNode> rootConstraintNodeFragments = FlatMappingSpliterator.flatMap(rule.constraints.stream(),
            c -> convertConstraint(c).stream())
            .iterator();

        return ConstraintNode.merge(rootConstraintNodeFragments);
    }

    private Collection<ConstraintNode> convertConstraint(Constraint constraintToConvert) {
        if (constraintToConvert instanceof NegatedGrammaticalConstraint) {
            Constraint negatedConstraint = ((NegatedGrammaticalConstraint) constraintToConvert).negatedConstraint;

            // ¬AND(X, Y, Z) reduces to OR(¬X, ¬Y, ¬Z)
            if (negatedConstraint instanceof AndConstraint) {
                Collection<Constraint> subConstraints = ((AndConstraint) negatedConstraint).subConstraints;

                return convertConstraint(
                    new OrConstraint(negateEach(subConstraints)));
            }
            // ¬OR(X, Y, Z) reduces to AND(¬X, ¬Y, ¬Z)
            else if (negatedConstraint instanceof OrConstraint) {
                Collection<Constraint> subConstraints = ((OrConstraint) negatedConstraint).subConstraints;

                return convertConstraint(
                    new AndConstraint(negateEach(subConstraints)));
            }
            // ¬IF(X, then: Y) reduces to AND(X, ¬Y)
            // ¬IF(X, then: Y, else: Z) reduces to OR(AND(X, ¬Y), AND(¬X, ¬Z))
            else if (negatedConstraint instanceof ConditionalConstraint) {
                ConditionalConstraint conditional = (ConditionalConstraint) negatedConstraint;

                Constraint positiveNegation = new AndConstraint(
                    conditional.condition,
                    conditional.whenConditionIsTrue.negate());

                Constraint negativeNegation = conditional.whenConditionIsFalse == null
                    ? null
                    : new AndConstraint(
                    conditional.condition.negate(),
                    conditional.whenConditionIsFalse.negate());

                return convertConstraint(
                    negativeNegation != null
                        ? positiveNegation.or(negativeNegation)
                        : positiveNegation
                );
            }
            // if we got this far, it must be an atomic constraint
            else {
                AtomicConstraint atomicConstraint = (AtomicConstraint) constraintToConvert;
                return asConstraintNodeList(atomicConstraint);
            }
        }
        // AND(X, Y, Z) becomes a flattened list of constraint nodes
        else if (constraintToConvert instanceof AndConstraint) {
            Collection<Constraint> subConstraints = ((AndConstraint) constraintToConvert).subConstraints;

            return FlatMappingSpliterator.flatMap(subConstraints.stream(),
                c -> convertConstraint(c).stream())
                .collect(Collectors.toList());
        }
        // OR(X, Y, Z) becomes a decision node
        else if (constraintToConvert instanceof OrConstraint) {
            Collection<Constraint> subConstraints = ((OrConstraint) constraintToConvert).subConstraints;

            DecisionNode decisionPoint = new TreeDecisionNode(
                subConstraints.stream()
                    .map(c -> ConstraintNode.merge(convertConstraint(c).stream().iterator()))
                    .collect(Collectors.toList()));

            return asConstraintNodeList(decisionPoint);
        } else if (constraintToConvert instanceof ConditionalConstraint) {
            return convertConstraint(reduceConditionalConstraint((ConditionalConstraint) constraintToConvert));
        } else {
            AtomicConstraint atomicConstraint = (AtomicConstraint) constraintToConvert;
            return asConstraintNodeList(atomicConstraint);
        }
    }
}

