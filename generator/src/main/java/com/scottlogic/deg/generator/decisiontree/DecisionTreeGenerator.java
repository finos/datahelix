package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeGenerator implements IDecisionTreeGenerator {
    private final DecisionTreeSimplifier decisionTreeSimplifier = new DecisionTreeSimplifier();

    @Override
    public IDecisionTreeProfile analyse(Profile profile) {
        return new DecisionTreeProfile(
            profile.fields,
            profile.rules.stream()
                .map(r -> new RuleDecisionTree(r.description, convertRule(r)))
                .map(decisionTreeSimplifier::simplify)
                .collect(Collectors.toList()));
    }

    private RuleOption convertRule(Rule rule) {
        Iterator<RuleOption> rootOptionNodeFragments = rule.constraints.stream()
            .flatMap(c -> convertConstraint(c).stream())
            .iterator();

        return RuleOption.merge(rootOptionNodeFragments);
    }

    private Collection<RuleOption> convertConstraint(IConstraint constraint) {
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
                return asOptionNodeList(constraint);
            }
        }
        // AND(X, Y, Z) becomes a flattened list of option nodes
        else if (constraint instanceof AndConstraint) {
            Collection<IConstraint> subConstraints = ((AndConstraint) constraint).subConstraints;

            return subConstraints.stream()
                .flatMap(c -> convertConstraint(c).stream())
                .collect(Collectors.toList());
        }
        // OR(X, Y, Z) becomes a decision node
        else if (constraint instanceof OrConstraint) {
            Collection<IConstraint> subConstraints = ((OrConstraint) constraint).subConstraints;

            RuleDecision decisionPoint = new RuleDecision(
                subConstraints.stream()
                    .map(c -> RuleOption.merge(convertConstraint(c).stream().iterator()))
                    .collect(Collectors.toList()));

            return asOptionNodeList(decisionPoint);
        }
        else if (constraint instanceof ConditionalConstraint) {
            return convertConstraint(reduceConditionalConstraint(constraint));
        }
        else {
            return asOptionNodeList(constraint);
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

    private static Collection<RuleOption> asOptionNodeList(Collection<IConstraint> constraints) {
        return Collections.singleton(
            new RuleOption(
                constraints,
                Collections.emptyList()));
    }

    private static Collection<RuleOption> asOptionNodeList(IConstraint constraint) {
        return asOptionNodeList(Collections.singleton(constraint));
    }

    private static Collection<RuleOption> asOptionNodeList(RuleDecision decision) {
        return Collections.singleton(
            new RuleOption(
                Collections.emptyList(),
                Collections.singleton(decision)));
    }

    class DecisionTreeSimplifier {
        public IRuleDecisionTree simplify(IRuleDecisionTree originalTree) {
            return new RuleDecisionTree(
                originalTree.getDescription(),
                simplify(originalTree.getRootOption()));
        }

        private IRuleOption simplify(IRuleOption option) {
            if (option.getDecisions().isEmpty())
                return option;

            return new RuleOption(
                option.getAtomicConstraints(),
                option.getDecisions().stream()
                    .map(this::simplify)
                    .collect(Collectors.toList()));
        }

        private IRuleDecision simplify(IRuleDecision decision) {
            List<IRuleOption> newOptions = new ArrayList<>();

            for (IRuleOption existingOption : decision.getOptions()) {
                IRuleOption simplifiedOption = simplify(existingOption);

                // if an option contains no constraints and only one decision, then it can be replaced by the set of options within that decision.
                // this helps simplify the sorts of trees that come from eg A OR (B OR C)
                if (simplifiedOption.getAtomicConstraints().isEmpty() && simplifiedOption.getDecisions().size() == 1) {
                    newOptions.addAll(
                        simplifiedOption.getDecisions()
                            .iterator().next() //get only member
                            .getOptions());
                }
                else {
                    newOptions.add(simplifiedOption);
                }
            }

            return new RuleDecision(newOptions);
        }
    }
}

