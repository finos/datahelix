package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class ElseBuilder extends BaseConstraintBuilder<ConditionalConstraint> {
    private Constraint ifCondition;
    private Constraint thenCondition;
    private Constraint elseCondition;

    public ElseBuilder(Constraint ifCondition, ConstraintChainBuilder<? extends Constraint> builder) {
        this.ifCondition = ifCondition;
        thenCondition = builder.copy().build();
    }

    public ElseBuilder(Constraint ifCondition, ElseBuilder builder) {
        this.ifCondition = ifCondition;
        thenCondition = builder.copy().buildInner();
    }

    private ElseBuilder(Constraint ifCondition, Constraint thenCondition, Constraint elseCondition) {
        this.ifCondition = ifCondition;
        this.thenCondition = thenCondition;
        this.elseCondition = elseCondition;
    }

    public ElseBuilder withElse(ConstraintChainBuilder<? extends Constraint> builder) {
        elseCondition = builder.copy().build();
        return this.copy();
    }

    public ElseBuilder withElse(ElseBuilder builder) {
        elseCondition = builder.copy().buildInner();
        return this.copy();
    }

    @Override
    public ConditionalConstraint buildInner() {
        return new ConditionalConstraint(ifCondition, thenCondition, elseCondition);
    }

    public ElseBuilder copy() {
        return new ElseBuilder(ifCondition, thenCondition, elseCondition);
    }
}
