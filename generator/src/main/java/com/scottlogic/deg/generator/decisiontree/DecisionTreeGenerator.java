package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeGenerator implements IDecisionTreeGenerator {
    private final DecisionTreeSimplifier decisionTreeSimplifier = new DecisionTreeSimplifier();

    @Override
    public DecisionTreeProfile analyse(Profile profile) {

        List<ConstraintNode> rootNodes = profile.rules
            .stream()
            .map(this::convertRule)
            .collect(Collectors.toList());

        ConstraintNode rootNode = ConstraintNode.merge(rootNodes.iterator());

        return new DecisionTreeProfile(
            profile.fields,
            decisionTreeSimplifier.simplify(rootNode));
    }

    private ConstraintNode convertRule(Rule rule) {
        Iterator<ConstraintNode> rootConstraintNodeFragments = rule.constraints.stream()
            .flatMap(c -> convertConstraint(c).stream())
            .iterator();

        return ConstraintNode.merge(rootConstraintNodeFragments);
    }

    private Collection<ConstraintNode> convertConstraint(IConstraint constraint) {
        if (constraint instanceof NotConstraint) {
            IConstraint negatedConstraint = ((NotConstraint) constraint).negatedConstraint;

            // ¬¬X reduces to X
            if (negatedConstraint instanceof NotConstraint) {
                return convertConstraint(((NotConstraint) negatedConstraint).negatedConstraint);
            }
            // ¬AND(X, Y, Z) reduces to OR(¬X, ¬Y, ¬Z)
            else if (negatedConstraint instanceof AndConstraint) {
                Collection<IConstraint> subConstraints = ((AndConstraint) negatedConstraint).subConstraints;

                return convertConstraint(new OrConstraint(
                    subConstraints.stream()
                        .map(NotConstraint::new)
                        .collect(Collectors.toList())));
            }
            // ¬OR(X, Y, Z) reduces to AND(¬X, ¬Y, ¬Z)
            else if (negatedConstraint instanceof OrConstraint) {
                Collection<IConstraint> subConstraints = ((OrConstraint) negatedConstraint).subConstraints;

                return convertConstraint(new AndConstraint(
                    subConstraints.stream()
                        .map(NotConstraint::new)
                        .collect(Collectors.toList())));
            }
            // ¬IF(X, then: Y) reduces to AND(X, ¬Y)
            // ¬IF(X, then: Y, else: Z) reduces to OR(AND(X, ¬Y), AND(¬X, ¬Z))
            else if (negatedConstraint instanceof ConditionalConstraint) {
                ConditionalConstraint conditional = (ConditionalConstraint) negatedConstraint;

                IConstraint positiveNegation = new AndConstraint(
                    conditional.condition,
                    new NotConstraint(conditional.whenConditionIsTrue));

                IConstraint negativeNegation = conditional.whenConditionIsFalse == null
                    ? null
                    : new AndConstraint(
                        new NotConstraint(conditional.condition),
                        new NotConstraint(conditional.whenConditionIsFalse));

                return convertConstraint(
                    negativeNegation != null
                        ? positiveNegation.or(negativeNegation)
                        : positiveNegation);
            }
            else {
                return asConstraintNodeList(constraint);
            }
        }
        // AND(X, Y, Z) becomes a flattened list of constraint nodes
        else if (constraint instanceof AndConstraint) {
            Collection<IConstraint> subConstraints = ((AndConstraint) constraint).subConstraints;

            return subConstraints.stream()
                .flatMap(c -> convertConstraint(c).stream())
                .collect(Collectors.toList());
        }
        // OR(X, Y, Z) becomes a decision node
        else if (constraint instanceof OrConstraint) {
            Collection<IConstraint> subConstraints = ((OrConstraint) constraint).subConstraints;

            DecisionNode decisionPoint = new DecisionNode(
                subConstraints.stream()
                    .map(c -> ConstraintNode.merge(convertConstraint(c).stream().iterator()))
                    .collect(Collectors.toList()));

            return asConstraintNodeList(decisionPoint);
        }
        else if (constraint instanceof ConditionalConstraint) {
            return convertConstraint(reduceConditionalConstraint(constraint));
        }
        else {
            return asConstraintNodeList(constraint);
        }
    }

    private static IConstraint reduceConditionalConstraint(IConstraint constraint) {
        ConditionalConstraint constraintAsCondition = ((ConditionalConstraint) constraint);
        IConstraint ifConstraint = constraintAsCondition.condition;
        IConstraint thenConstraint = constraintAsCondition.whenConditionIsTrue;
        IConstraint elseConstraint = constraintAsCondition.whenConditionIsFalse;

        return new OrConstraint(
            ifConstraint.and(thenConstraint),
            elseConstraint != null
                ? ifConstraint.isFalse().and(elseConstraint)
                : ifConstraint.isFalse());
    }

    private static Collection<ConstraintNode> asConstraintNodeList(Collection<IConstraint> constraints) {
        return Collections.singleton(
            new ConstraintNode(
                constraints,
                Collections.emptyList()));
    }

    private static Collection<ConstraintNode> asConstraintNodeList(IConstraint constraint) {
        return asConstraintNodeList(Collections.singleton(constraint));
    }

    private static Collection<ConstraintNode> asConstraintNodeList(DecisionNode decision) {
        return Collections.singleton(
            new ConstraintNode(
                Collections.emptyList(),
                Collections.singleton(decision)));
    }

    class DecisionTreeSimplifier {
        public ConstraintNode simplify(ConstraintNode node) {
            if (node.getDecisions().isEmpty())
                return node;

            return new ConstraintNode(
                node.getAtomicConstraints(),
                node.getDecisions().stream()
                    .map(this::simplify)
                    .collect(Collectors.toList()));
        }

        private DecisionNode simplify(DecisionNode decision) {
            List<ConstraintNode> newNodes = new ArrayList<>();

            for (ConstraintNode existingOption : decision.getOptions()) {
                ConstraintNode simplifiedNode = simplify(existingOption);

                // if an option contains no constraints and only one decision, then it can be replaced by the set of options within that decision.
                // this helps simplify the sorts of trees that come from eg A OR (B OR C)
                if (simplifiedNode.getAtomicConstraints().isEmpty() && simplifiedNode.getDecisions().size() == 1) {
                    newNodes.addAll(
                        simplifiedNode.getDecisions()
                            .iterator().next() //get only member
                            .getOptions());
                }
                else {
                    newNodes.add(simplifiedNode);
                }
            }

            return new DecisionNode(newNodes);
        }
    }
}

