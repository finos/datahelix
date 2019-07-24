package com.scottlogic.deg.generator.walker.rowspec;

import com.google.inject.Inject;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.Optional;
import java.util.stream.Stream;

public class RandomRowSpecDecisionTreeWalker implements DecisionTreeWalker {
    private final RowSpecTreeSolver rowSpecTreeSolver;
    private final RowSpecDataBagGenerator rowSpecDataBagGenerator;

    @Inject
    public RandomRowSpecDecisionTreeWalker(RowSpecTreeSolver rowSpecTreeSolver, RowSpecDataBagGenerator rowSpecDataBagGenerator) {
        this.rowSpecTreeSolver = rowSpecTreeSolver;
        this.rowSpecDataBagGenerator = rowSpecDataBagGenerator;
    }

    @Override
    public Stream<DataBag> walk(DecisionTree tree) {

        if (tree.rootNode.getDecisions().isEmpty()){
            return generateWithoutRestarting(tree);
        }

        return FlatMappingSpliterator.flatMap(
            getRowSpecAndRestart(tree),
            this::createDataBags);
    }

    private Stream<DataBag> generateWithoutRestarting(DecisionTree tree) {
        return FlatMappingSpliterator.flatMap(
            rowSpecTreeSolver.createRowSpecs(tree),
            rowSpecDataBagGenerator::createDataBags);
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

    private Stream<DataBag> createDataBags(RowSpec rowSpec) {
        return rowSpecDataBagGenerator.createDataBags(rowSpec)
            .limit(1);
    }
}
