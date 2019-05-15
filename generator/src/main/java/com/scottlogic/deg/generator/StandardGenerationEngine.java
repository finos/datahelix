package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.validators.AncestralContradictionDecisionTreeValidator;
import com.scottlogic.deg.generator.validators.StaticContradictionDecisionTreeValidator;

import java.io.IOException;
import java.util.stream.Stream;

public class StandardGenerationEngine implements GenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private ReductiveDataGeneratorMonitor monitor;
    private final DataGenerator dataGenerator;
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public StandardGenerationEngine(
        DataGenerator dataGenerator,
        DecisionTreeFactory decisionTreeGenerator,
        ReductiveDataGeneratorMonitor monitor,
        FieldSpecFactory fieldSpecFactory,
        FieldSpecMerger fieldSpecMerger) {
        this.dataGenerator = dataGenerator;
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.monitor = monitor;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public void generateDataSet(Profile profile, GenerationConfig config, OutputTarget outputTarget) throws IOException {
        final DecisionTree decisionTree = this.decisionTreeGenerator.analyse(profile);

        // check the if decision tree is wholly contradictory
        // stop execusion, output warning, and blank Dataset

        // check the if the decision tree has any contradictions
        // warn and continue
        //StaticContradictionDecisionTreeValidator treeValidator =
          //  new StaticContradictionDecisionTreeValidator(
           //     profile.fields,
            //    new RowSpecMerger(fieldSpecMerger),
            //    new ConstraintReducer(fieldSpecFactory, fieldSpecMerger));

        //DecisionTree markedTree = treeValidator.markContradictions(decisionTree);

        AncestralContradictionDecisionTreeValidator ac =
            new AncestralContradictionDecisionTreeValidator(
                new ConstraintReducer(
                    new FieldSpecFactory(
                        new FieldSpecMerger(),
                        new StringRestrictionsFactory()),
                    new FieldSpecMerger()));

        ac.markContradictions(decisionTree);

        // prune tree of contradictory nodes

        final Stream<GeneratedObject> generatedDataItems =
            this.dataGenerator.generateData(profile, decisionTree, config);

        outputTarget.outputDataset(generatedDataItems, profile.fields);

        monitor.endGeneration();
    }
}
