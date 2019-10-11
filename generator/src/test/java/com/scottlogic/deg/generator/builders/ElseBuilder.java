/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;

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

    public ElseBuilder negate() {
        if (elseCondition == null) {
            return new ElseBuilder(ifCondition, thenCondition.negate(), null);
        }

        return new ElseBuilder(ifCondition, thenCondition, elseCondition.negate());
    }

    @Override
    public ConditionalConstraint build() {
        return new ConditionalConstraint(ifCondition, thenCondition, elseCondition);
    }
}
