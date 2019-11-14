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
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.datahelix.generator.common.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.decisionbased.RowSpecTreeSolver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomRowSpecDecisionTreeWalker implements DecisionTreeWalker {
    private final RowSpecTreeSolver rowSpecTreeSolver;
    private final RowSpecDataBagGenerator rowSpecDataBagGenerator;
    private PotentialRowSpecCount potentialRowSpecCount;
    private final JavaUtilRandomNumberGenerator random;

    @Inject
    public RandomRowSpecDecisionTreeWalker(RowSpecTreeSolver rowSpecTreeSolver,
                                           RowSpecDataBagGenerator rowSpecDataBagGenerator,
                                           PotentialRowSpecCount potentialRowSpecCount,
                                           JavaUtilRandomNumberGenerator random) {
        this.rowSpecTreeSolver = rowSpecTreeSolver;
        this.rowSpecDataBagGenerator = rowSpecDataBagGenerator;
        this.potentialRowSpecCount = potentialRowSpecCount;
        this.random = random;
    }

    @Override
    public Stream<DataBag> walk(DecisionTree tree) {
        if (tree.rootNode.getDecisions().isEmpty()) {
            return generateWithoutRestarting(tree);
        }
        boolean useCache = potentialRowSpecCount.lessThanMax(tree);
        Stream<RowSpec> rowSpecStream = useCache  ? getFromCachedRowSpecs(tree): getRowSpecAndRestart(tree);

        return rowSpecStream.map(this::createDataBag);
    }

    private Stream<RowSpec> getFromCachedRowSpecs(DecisionTree tree) {
        List<RowSpec> rowSpecCache = rowSpecTreeSolver.createRowSpecs(tree).collect(Collectors.toList());
        return Stream.generate(() -> getRandomRowSpec(rowSpecCache));
    }

    private Stream<DataBag> generateWithoutRestarting(DecisionTree tree) {
        RowSpec rowSpec = getFirstRowSpec(tree).get();
        return rowSpecDataBagGenerator.createDataBags(rowSpec);
    }

    private Stream<RowSpec> getRowSpecAndRestart(DecisionTree tree) {
        Optional<RowSpec> firstRowSpecOpt = getFirstRowSpec(tree);
        if (!firstRowSpecOpt.isPresent()) {
            return Stream.empty();
        }

        return Stream.generate(() -> getFirstRowSpec(tree))
            .map(Optional::get);
    }

    private Optional<RowSpec> getFirstRowSpec(DecisionTree tree) {
        return rowSpecTreeSolver.createRowSpecs(tree).findFirst();
    }

    private RowSpec getRandomRowSpec(List<RowSpec> rowSpecCache) {
        return rowSpecCache.get(random.nextInt(rowSpecCache.size()));
    }

    private DataBag createDataBag(RowSpec rowSpec) {
        return rowSpecDataBagGenerator.createDataBags(rowSpec).findFirst().get();
    }
}
