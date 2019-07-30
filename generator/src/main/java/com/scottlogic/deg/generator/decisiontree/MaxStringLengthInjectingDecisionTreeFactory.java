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

package com.scottlogic.deg.generator.decisiontree;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Decorator over a DecisionTreeFactory to inject a &lt;shorterThan X&gt; constraint at the root node for every field
 */
public class MaxStringLengthInjectingDecisionTreeFactory implements DecisionTreeFactory{
    private final int maxLength;
    private final DecisionTreeFactory underlyingFactory;

    @Inject
    public MaxStringLengthInjectingDecisionTreeFactory(ProfileDecisionTreeFactory underlyingFactory) {
        this(underlyingFactory, Defaults.MAX_STRING_LENGTH);
    }

    public MaxStringLengthInjectingDecisionTreeFactory(DecisionTreeFactory underlyingFactory, int maxLength) {
        this.underlyingFactory = underlyingFactory;
        this.maxLength = maxLength;
    }

    @Override
    public DecisionTree analyse(Profile profile) {
        DecisionTree tree = underlyingFactory.analyse(profile);

        Set<RuleInformation> rules = Collections.singleton(createRule());

        return new DecisionTree(
            tree.rootNode.builder().addAtomicConstraints(
                tree.fields
                    .stream()
                    .map(field -> new IsStringShorterThanConstraint(field, maxLength + 1))
                    .collect(Collectors.toList())
            ).build(),
            tree.fields
        );
    }

    private RuleInformation createRule() {
        return new RuleInformation("Auto-injected: String-max-length");
    }
}
