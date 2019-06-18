package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class ReductiveVisualiseDecisionTreeWalker extends ReductiveDecisionTreeWalker {

    private final ReductiveIterationVisualiser iterationVisualiser;

    @Inject
    public ReductiveVisualiseDecisionTreeWalker(ReductiveIterationVisualiser iterationVisualiser, ReductiveFieldSpecBuilder reductiveFieldSpecBuilder, ReductiveDataGeneratorMonitor monitor, ReductiveTreePruner treePruner, FieldSpecValueGenerator fieldSpecValueGenerator, FixFieldStrategyFactory fixFieldStrategyFactory) {
        super(reductiveFieldSpecBuilder, monitor, treePruner, fieldSpecValueGenerator, fixFieldStrategyFactory);
        this.iterationVisualiser = iterationVisualiser;
    }

    @Override
    protected Stream<DataBag> fixNextField(ConstraintNode tree, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {
        visualise(tree, reductiveState);

        return super.fixNextField(tree, reductiveState, fixFieldStrategy);
    }

    private void visualise(ConstraintNode rootNode, ReductiveState reductiveState){
        try {
            iterationVisualiser.visualise(rootNode, reductiveState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
