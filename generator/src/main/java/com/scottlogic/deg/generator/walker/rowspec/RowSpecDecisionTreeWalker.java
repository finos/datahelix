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
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.decisionbased.RowSpecTreeSolver;

import java.util.stream.Stream;

public class RowSpecDecisionTreeWalker implements DecisionTreeWalker {

    private final RowSpecTreeSolver rowSpecTreeSolver;
    private final RowSpecDataBagGenerator rowSpecDataBagGenerator;

    @Inject
    public RowSpecDecisionTreeWalker(RowSpecTreeSolver rowSpecTreeSolver, RowSpecDataBagGenerator rowSpecDataBagGenerator) {
        this.rowSpecTreeSolver = rowSpecTreeSolver;
        this.rowSpecDataBagGenerator = rowSpecDataBagGenerator;
    }

    @Override
    public Stream<DataBag> walk(DecisionTree tree) {
        return FlatMappingSpliterator.flatMap(
            rowSpecTreeSolver.createRowSpecs(tree),
            rowSpecDataBagGenerator::createDataBags);
    }
}
