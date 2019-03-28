package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class ElseBuilder extends BaseConstraintBuilder<ConditionalConstraint> {
    private final Constraint ifCondition;
    private final Constraint thenCondition;
    private final Constraint elseCondition;

    private ElseBuilder(Constraint ifCondition, Constraint thenCondition, Constraint elseCondition) {
        this.ifCondition = ifCondition;
        this.thenCondition = thenCondition;
        this.elseCondition = elseCondition;
    }

    ElseBuilder(Constraint ifCondition, BaseConstraintBuilder<? extends Constraint> builder) {
        this.ifCondition = ifCondition;
        this.thenCondition = builder.build();
        this.elseCondition = null;
    }

    public ElseBuilder withElse(BaseConstraintBuilder<? extends Constraint> builder) {
        return new ElseBuilder(ifCondition, thenCondition, builder.build());
    }

    @Override
    public ConditionalConstraint build() {
        return new ConditionalConstraint(ifCondition, thenCondition, elseCondition);
    }
}
