package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v3.RuleDTO;

import java.util.ArrayList;
import java.util.Collection;

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
