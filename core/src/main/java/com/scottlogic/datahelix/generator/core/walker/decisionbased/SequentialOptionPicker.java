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
package com.scottlogic.datahelix.generator.core.walker.decisionbased;

import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNode;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionNode;

import java.util.stream.Stream;

public class SequentialOptionPicker implements OptionPicker {
    @Override
    public DecisionNode pickDecision(ConstraintNode constraintNode) {
        return constraintNode.getDecisions().iterator().next();
    }

    @Override
    public Stream<ConstraintNode> streamOptions(DecisionNode decisionNode) {
        return decisionNode.getOptions().stream();
    }
}
