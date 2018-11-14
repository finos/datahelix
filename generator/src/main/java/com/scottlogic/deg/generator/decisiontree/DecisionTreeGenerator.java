package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DecisionTreeGenerator implements IDecisionTreeGenerator {
    private final DecisionTreeSimplifier decisionTreeSimplifier = new DecisionTreeSimplifier();

    private static Collection<IConstraint> wrapEach(Collection<IConstraint> constraints,
                                                    Function<IConstraint, IConstraint> wrapFunc) {
        return constraints.stream()
            .map(wrapFunc)
            .collect(Collectors.toList());
    }

    private static Collection<IConstraint> negateEach(Collection<IConstraint> constraints) {
        return wrapEach(constraints, NotConstraint::new);
    }

    private static Collection<IConstraint> violateEach(Collection<IConstraint> constraints) {
        return wrapEach(constraints, ViolateConstraint::new);
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
            new TreeConstraintNode(
                constraints,
                Collections.emptyList()));
    }

    private static Collection<ConstraintNode> asConstraintNodeList(IConstraint constraint) {
        return asConstraintNodeList(Collections.singleton(constraint));
    }

    private static Collection<ConstraintNode> asConstraintNodeList(DecisionNode decision) {
        return Collections.singleton(
            new TreeConstraintNode(
                Collections.emptyList(),
                Collections.singleton(decision)));
    }

    @Override
    public DecisionTreeCollection analyse(Profile profile) {
        return new DecisionTreeCollection(
            profile.fields,
            profile.rules.stream()
                .map(rule -> new DecisionTree(convertRule(rule), profile.fields, profile.description))
                .map(decisionTreeSimplifier::simplify)
                .collect(Collectors.toList()));
    }

    private ConstraintNode convertRule(Rule rule) {
        Iterator<ConstraintNode> rootConstraintNodeFragments = rule.constraints.stream()
            .flatMap(c -> convertConstraint(c).stream())
            .iterator();

        return TreeConstraintNode.merge(rootConstraintNodeFragments);
    }

    private Collection<ConstraintNode> convertConstraint(IConstraint constraintToConvert) {
        if (constraintToConvert instanceof ViolateConstraint) {
            IConstraint violatedConstraint = ((ViolateConstraint) constraintToConvert).violatedConstraint;

            // VIOLATE(AND(X, Y, Z)) reduces to
            //   OR(
            //     AND(VIOLATE(X), Y, Z),
            //     AND(X, VIOLATE(Y), Z),
            //     AND(X, Y, VIOLATE(Z)))
            if (violatedConstraint instanceof AndConstraint) {
                Collection<IConstraint> subConstraints = ((AndConstraint) violatedConstraint).subConstraints;

                Collection<IConstraint> possibleFulfilments =
                    subConstraints.stream()
                        // for each subconstraint X, make a copy of the original list but with X replaced by VIOLATE(X)
                        .map(constraintToViolate ->
                            subConstraints.stream()
                                .map(c -> c == constraintToViolate
                                    ? new ViolateConstraint(c)
                                    : c)
                                .collect(Collectors.toList()))
                        // make an AndConstraint out of each of the new lists
                        .map(AndConstraint::new)
                        .collect(Collectors.toList());

                return convertConstraint(new OrConstraint(possibleFulfilments));
            }
            // VIOLATE(OR(X, Y, Z)) reduces to AND(VIOLATE(X), VIOLATE(Y), VIOLATE(Z))
            else if (violatedConstraint instanceof OrConstraint) {
                Collection<IConstraint> subConstraints = ((OrConstraint) violatedConstraint).subConstraints;

                return convertConstraint(
                    new AndConstraint(violateEach(subConstraints)));
            }
            // VIOLATE(IF(X, then: Y)) reduces to AND(X, VIOLATE(Y))
            // VIOLATE(IF(X, then: Y, else: Z)) reduces to OR(AND(X, VIOLATE(Y)), AND(VIOLATE(X), VIOLATE(Z)))
            else if (violatedConstraint instanceof ConditionalConstraint) {
                ConditionalConstraint conditional = ((ConditionalConstraint) violatedConstraint);

                IConstraint positiveViolation = new AndConstraint(
                    conditional.condition,
                    new ViolateConstraint(conditional.whenConditionIsTrue));

                IConstraint negativeViolation = conditional.whenConditionIsFalse == null
                    ? null
                    : new AndConstraint(
                    new ViolateConstraint(conditional.condition),
                    new ViolateConstraint(conditional.whenConditionIsFalse));

                return convertConstraint(
                    negativeViolation != null
                        ? positiveViolation.or(negativeViolation)
                        : positiveViolation);
            }

            // we've got an atomic constraint
            return convertConstraint(new NotConstraint(violatedConstraint));
        } else if (constraintToConvert instanceof NotConstraint) {
            IConstraint negatedConstraint = ((NotConstraint) constraintToConvert).negatedConstraint;

            // ¬¬X reduces to X
            if (negatedConstraint instanceof NotConstraint) {
                return convertConstraint(((NotConstraint) negatedConstraint).negatedConstraint);
            }
            // ¬AND(X, Y, Z) reduces to OR(¬X, ¬Y, ¬Z)
            else if (negatedConstraint instanceof AndConstraint) {
                Collection<IConstraint> subConstraints = ((AndConstraint) negatedConstraint).subConstraints;

                return convertConstraint(
                    new OrConstraint(negateEach(subConstraints)));
            }
            // ¬OR(X, Y, Z) reduces to AND(¬X, ¬Y, ¬Z)
            else if (negatedConstraint instanceof OrConstraint) {
                Collection<IConstraint> subConstraints = ((OrConstraint) negatedConstraint).subConstraints;

                return convertConstraint(
                    new AndConstraint(negateEach(subConstraints)));
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
            // if we got this far, it must be an atomic constraint
            else {
                return asConstraintNodeList(constraintToConvert);
            }
        }
        // AND(X, Y, Z) becomes a flattened list of constraint nodes
        else if (constraintToConvert instanceof AndConstraint) {
            Collection<IConstraint> subConstraints = ((AndConstraint) constraintToConvert).subConstraints;

            return subConstraints.stream()
                .flatMap(c -> convertConstraint(c).stream())
                .collect(Collectors.toList());
        }
        // OR(X, Y, Z) becomes a decision node
        else if (constraintToConvert instanceof OrConstraint) {
            Collection<IConstraint> subConstraints = ((OrConstraint) constraintToConvert).subConstraints;

            DecisionNode decisionPoint = new TreeDecisionNode(
                subConstraints.stream()
                    .map(c -> TreeConstraintNode.merge(convertConstraint(c).stream().iterator()))
                    .collect(Collectors.toList()));

            return asConstraintNodeList(decisionPoint);
        } else if (constraintToConvert instanceof ConditionalConstraint) {
            return convertConstraint(reduceConditionalConstraint(constraintToConvert));
        } else {
            return asConstraintNodeList(constraintToConvert);
        }
    }

    class DecisionTreeSimplifier {
        public DecisionTree simplify(DecisionTree originalTree) {
            return new DecisionTree(
                simplify(originalTree.getRootNode()),
                originalTree.getFields(),
                originalTree.getDescription());
        }

        public ConstraintNode simplify(ConstraintNode node) {
            if (node.getDecisions().isEmpty())
                return node;

            return new TreeConstraintNode(
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
                } else {
                    newNodes.add(simplifiedNode);
                }
            }

            return new TreeDecisionNode(newNodes);
        }
    }
}

