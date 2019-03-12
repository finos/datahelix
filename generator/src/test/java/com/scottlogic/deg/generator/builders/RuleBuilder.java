package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v0_1.RuleDTO;

/**
 * Defines a builder for a Rule object
 */
public class RuleBuilder extends ConstraintChainBuilder<Rule> {
    private RuleInformation ruleInformation;

    public RuleBuilder(String ruleName) {
        this.ruleInformation = new RuleInformation(new RuleDTO(ruleName, null));
    }

    private RuleBuilder(RuleBuilder ruleBuilder) {
        super(ruleBuilder);
        ruleInformation = new RuleInformation(new RuleDTO(ruleBuilder.ruleInformation.getDescription(), null));
    }

    public Rule buildInner() {
        return new Rule(ruleInformation, constraints);
    }

    @Override
    ConstraintChainBuilder<Rule> copy() {
        return new RuleBuilder(this);
    }
}
