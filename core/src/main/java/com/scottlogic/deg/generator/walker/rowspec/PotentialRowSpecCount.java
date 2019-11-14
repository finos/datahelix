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

package com.scottlogic.deg.generator.walker.rowspec;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public class PotentialRowSpecCount {
    private final int max;

    @Inject
    public PotentialRowSpecCount(@Named("config:internalRandomRowSpecStorage") int max) {
        this.max = max;
    }

    /**
     *  recursively traverses the tree counting the maximum potential number of
     *  decisions that could result. Breaks early if count goes over given max value
     * @param decisionTree tree to count
     * @return whether the tree has less than the max number of decisions
     */
    boolean lessThanMax(DecisionTree decisionTree){
        Integer total = countConstraintNode(decisionTree.rootNode);
        return total != null;
    }

    private Integer countConstraintNode(ConstraintNode constraintNode){
        int total = 1;
        for (DecisionNode decision : constraintNode.getDecisions()) {
            Integer count = countDecisionNode(decision);
            if (count == null) return null;

            total *= count;
            if (total > max) return null;
        }
        return total;
    }

    private Integer countDecisionNode(DecisionNode decision) {
        int total = 0;
        for (ConstraintNode option : decision.getOptions()) {
            Integer count = countConstraintNode(option);
            if (count == null) return null;

            total += count;
            if (total > max) return null;
        }
        return total;
    }
}