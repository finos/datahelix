package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class ElseBuilder extends BaseConstraintBuilder<ConditionalConstraint> {
    private Constraint ifCondition;
    private Constraint thenCondition;
    private Constraint elseCondition;

    public ElseBuilder(Constraint ifCondition, ConstraintChainBuilder<? extends Constraint> builder) {
        this.ifCondition = ifCondition;
        thenCondition = builder.build();
    }

    public BaseConstraintBuilder<ConditionalConstraint> withElse(ConstraintChainBuilder<? extends Constraint> builder) {
        elseCondition = builder.build();
        return this;
    }

    @Override
    ConditionalConstraint buildInner() {
        return new ConditionalConstraint(ifCondition, thenCondition, elseCondition);
    }
}
