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
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;

import java.util.List;

public class OrBuilder extends ConstraintChainBuilder<OrConstraint> {
    public OrBuilder() {
        super();
    }

    private OrBuilder(Constraint headConstraint, List<Constraint> tailConstraints) {
        super(headConstraint, tailConstraints);
    }

    public OrConstraint buildInner() {
        return new OrConstraint(tailConstraints);
    }

    @Override
    ConstraintChainBuilder<OrConstraint> create(Constraint headConstraint, List<Constraint> tailConstraints) {
        return new OrBuilder(headConstraint, tailConstraints);
    }
}
