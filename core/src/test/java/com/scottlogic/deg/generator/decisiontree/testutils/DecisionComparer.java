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

package com.scottlogic.deg.generator.decisiontree.testutils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Objects;

public class DecisionComparer implements EqualityComparer {
    private final static AnyOrderCollectionEqualityComparer optionAnyOrderComparer
        = new AnyOrderCollectionEqualityComparer(new OptionEqualityComparer());

    @Override
    public int getHashCode(Object decision){
        return getHashCode((DecisionNode)decision);
    }

    public int getHashCode(DecisionNode decision){
        return decision
            .getOptions()
            .stream()
            .reduce(
                0,
                (prev, option) -> prev * option.hashCode(),
                (prevHash, optionHash) -> prevHash * optionHash);
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((DecisionNode)item1, (DecisionNode)item2);
    }

    public boolean equals(DecisionNode decision1, DecisionNode decision2){
        if (decision1 == null && decision2 == null)
            return true;

        if (decision1 == null || decision2 == null)
            return false; //either decision1 XOR decision2 is null

        return decision1.getOptions().size() == decision2.getOptions().size()
            && optionAnyOrderComparer.equals(decision1.getOptions(), decision2.getOptions());
    }

    static class OptionEqualityComparer implements EqualityComparer{

        private final AnyOrderCollectionEqualityComparer atomicConstraintComparer =
            new AnyOrderCollectionEqualityComparer();

        @Override
        public int getHashCode(Object item) {
            ConstraintNode option = (ConstraintNode) item;
            return Objects.hash(option.getAtomicConstraints());
        }

        @Override
        public boolean equals(Object item1, Object item2) {
            ConstraintNode option1 = (ConstraintNode) item1;
            ConstraintNode option2 = (ConstraintNode) item2;

            return atomicConstraintComparer.equals(
                option1.getAtomicConstraints(),
                option2.getAtomicConstraints());
        }
    }
}
