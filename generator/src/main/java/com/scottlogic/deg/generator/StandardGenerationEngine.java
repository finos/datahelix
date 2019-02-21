package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Guice.CurrentProfileCache;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class StandardGenerationEngine implements GenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private ReductiveDataGeneratorMonitor monitor;
    private CurrentProfileCache cache;
    private final DataGenerator dataGenerator;

    @Inject
    public StandardGenerationEngine(
        CurrentProfileCache cache,
        DataGenerator dataGenerator,
        DecisionTreeFactory decisionTreeGenerator,
        ReductiveDataGeneratorMonitor monitor) {
        this.cache = cache;
        this.dataGenerator = dataGenerator;
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.monitor = monitor;
    }

    public void generateDataSet(Profile profile, GenerationConfig config, OutputTarget outputTarget) throws IOException {
        //Setting the current profile at the top level of generation for use deep within the data generation
        cache.profile = profile;

        final DecisionTreeCollection analysedProfile = this.decisionTreeGenerator.analyse(profile);

        final Stream<GeneratedObject> generatedDataItems =
            this.dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);

        outputTarget.outputDataset(generatedDataItems, profile.fields);

        monitor.endGeneration();
    }
}
