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

import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.RuleInformation;

import java.util.List;

/**
 * Defines a builder for a Rule object
 */
public class RuleBuilder extends ConstraintChainBuilder<Rule> {
    private final RuleInformation ruleInformation;

    public RuleBuilder(String ruleName) {
        this.ruleInformation = new RuleInformation(ruleName);
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
