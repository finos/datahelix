package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v0_1.RuleDTO;

import java.util.List;

/**
 * Defines a builder for a Rule object
 */
public class RuleBuilder extends ConstraintChainBuilder<Rule> {
    private final RuleInformation ruleInformation;

    public RuleBuilder(String ruleName) {
        this.ruleInformation = new RuleInformation(new RuleDTO(ruleName, null));
    }

    private RuleBuilder(Constraint headConstraint, List<Constraint> tailConstraints, RuleInformation ruleInformation) {
        super(headConstraint, tailConstraints);
        this.ruleInformation = ruleInformation;
    }

    public Rule buildInner() {
        return new Rule(ruleInformation, tailConstraints);
    }

    @Override
    ConstraintChainBuilder<Rule> create(Constraint headConstraint, List<Constraint> tailConstraints) {
        return new RuleBuilder(headConstraint, tailConstraints, ruleInformation);
    }
}
