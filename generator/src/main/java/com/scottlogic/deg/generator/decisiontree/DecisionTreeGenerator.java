package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.constraints.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class DecisionTreeGenerator implements IDecisionTreeGenerator {
    @Override
    public IDecisionTreeProfile analyse(Profile profile) {
        ArrayList<RuleDecisionTree> ruleDecisionTrees = new ArrayList<>();
        for (Rule rule : profile.rules) {
            ruleDecisionTrees.add(analyseRule(rule));
        }
        return new DecisionTreeProfile(profile.fields, ruleDecisionTrees);
    }

    private RuleDecisionTree analyseRule(Rule rule) {
        return new RuleDecisionTree(rule.description, getRuleOptionForConstraintCollection(rule.constraints));
    }

    private boolean isConstraintAtomic(IConstraint constraint) {
        if (constraint instanceof NotConstraint) {
            return isConstraintAtomic(((NotConstraint)constraint).negatedConstraint);
        }
        if (constraint instanceof AndConstraint || isConstraintDecisionPoint(constraint)) {
            return false;
        }
        return true;
    }

    private boolean isConstraintDecisionPoint(IConstraint constraint) {
        return (constraint instanceof OrConstraint || constraint instanceof ConditionalConstraint);
    }

    private RuleOption getRuleOptionForConstraint(IConstraint constraint) {
        constraint = unwrapNotConstraint(constraint);
        if (isConstraintAtomic(constraint)) {
            return new RuleOption(constraint);
        }
        if (isConstraintDecisionPoint(constraint)) {
            return new RuleOption(getRuleDecisionForConstraint(constraint));
        }
        else { // is AndConstraint
            return getRuleOptionForAndConstraint((AndConstraint)constraint);
        }
    }

    private IConstraint unwrapNotConstraint(IConstraint constraint) {
        if (!(constraint instanceof NotConstraint)) {
            return constraint;
        }
        NotConstraint nc = (NotConstraint)constraint;
        if (nc.negatedConstraint instanceof NotConstraint) {
            return unwrapNotConstraint(((NotConstraint)nc.negatedConstraint).negatedConstraint);
        }
        if (nc.negatedConstraint instanceof AndConstraint) {
            return new OrConstraint(((AndConstraint)nc.negatedConstraint).subConstraints
                    .stream()
                    .map(NotConstraint::new)
                    .collect(Collectors.toList()));
        }
        if (nc.negatedConstraint instanceof OrConstraint) {
            return new AndConstraint(((OrConstraint)nc.negatedConstraint).subConstraints
                    .stream()
                    .map(NotConstraint::new)
                    .collect(Collectors.toList()));
        }
        // This is a bit of a weird concept but provided for completeness.
        if (nc.negatedConstraint instanceof ConditionalConstraint) {
            ConditionalConstraint conditionalConstraint = (ConditionalConstraint)nc.negatedConstraint;
            IConstraint unwrappedCondition = unwrapNotConstraint(conditionalConstraint.condition);
            IConstraint firstBranch =
                    unwrappedCondition.and(unwrapNotConstraint(new NotConstraint(conditionalConstraint.whenConditionIsTrue)));
            if (conditionalConstraint.whenConditionIsFalse != null) {
                return firstBranch.or(
                        unwrapNotConstraint(new NotConstraint(unwrappedCondition))
                                .and(unwrapNotConstraint(new NotConstraint(conditionalConstraint.whenConditionIsFalse))));
            }
            return firstBranch;
        }

        // This method cannot unwrap all possible kinds of NotConstraint.
        return constraint;
    }

    private RuleOption getRuleOptionForTwoConstraints(IConstraint constraintA, IConstraint constraintB) {
        return getRuleOptionForConstraint(constraintA).merge(getRuleOptionForConstraint(constraintB));
    }

    private RuleOption getRuleOptionForConstraintCollection(Collection<IConstraint> constraintCollection) {
        ArrayList<IConstraint> atomicConstraints = new ArrayList<>();
        ArrayList<IRuleDecision> decisions = new ArrayList<>();
        ArrayList<RuleOption> mergeableOptions = new ArrayList<>();
        for (IConstraint subConstraint : constraintCollection) {
            subConstraint = unwrapNotConstraint(subConstraint);
            if (isConstraintAtomic(subConstraint)) {
                atomicConstraints.add(subConstraint);
            }
            else if (isConstraintDecisionPoint(subConstraint)) {
                decisions.add(getRuleDecisionForConstraint(subConstraint));
            }
            else { // is AndConstraint
                mergeableOptions.add(getRuleOptionForAndConstraint((AndConstraint)subConstraint));
            }
        }
        RuleOption option = new RuleOption(atomicConstraints, decisions);
        for (RuleOption toMerge : mergeableOptions) {
            option.merge(toMerge);
        }

        return option;
    }

    private RuleDecision getRuleDecisionForConstraint(IConstraint constraint) {
        if (constraint instanceof OrConstraint) {
            return getRuleDecisionForOrConstraint((OrConstraint)constraint);
        }
        if (constraint instanceof ConditionalConstraint) {
            return getRuleDecisionForConditionalConstraint((ConditionalConstraint)constraint);
        }
        throw new IllegalStateException("This code should never be reached.");
    }

    private RuleOption getRuleOptionForAndConstraint(AndConstraint constraint) {
        return getRuleOptionForConstraintCollection(constraint.subConstraints);
    }

    private RuleDecision getRuleDecisionForOrConstraint(OrConstraint constraint) {
        ArrayList<IRuleOption> options = new ArrayList<>();
        for (IConstraint subConstraint : constraint.subConstraints) {
            if (subConstraint instanceof OrConstraint) {
                RuleDecision toMerge = getRuleDecisionForOrConstraint((OrConstraint)subConstraint);
                options.addAll(toMerge.getOptions());
            }
            else {
                options.add(getRuleOptionForConstraint(subConstraint));
            }
        }
        return new RuleDecision(options);
    }

    private RuleDecision getRuleDecisionForConditionalConstraint(ConditionalConstraint constraint) {
        RuleOption branchWhen = getRuleOptionForTwoConstraints(constraint.condition, constraint.whenConditionIsTrue);
        RuleOption branchWhenNot;
        NotConstraint negatedCondition = new NotConstraint(constraint.condition);
        if (constraint.whenConditionIsFalse == null) {
            branchWhenNot = getRuleOptionForConstraint(negatedCondition);
        }
        else {
            branchWhenNot = getRuleOptionForTwoConstraints(negatedCondition, constraint.whenConditionIsFalse);
        }
        return new RuleDecision(branchWhen, branchWhenNot);
    }
}
