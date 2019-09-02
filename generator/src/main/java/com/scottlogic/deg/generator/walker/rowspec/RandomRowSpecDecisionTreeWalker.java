package com.scottlogic.deg.generator.walker.rowspec;

import com.google.inject.Inject;
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
        if (tree.rootNode.getDecisions().isEmpty()) {
            return generateWithoutRestarting(tree);
        }

        return getRowSpecAndRestart(tree)
            .map(this::createDataBag);
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

    private DataBag createDataBag(RowSpec rowSpec) {
        return rowSpecDataBagGenerator.createDataBags(rowSpec).findFirst().get();
    }
}
