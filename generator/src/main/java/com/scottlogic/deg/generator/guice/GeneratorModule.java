package com.scottlogic.deg.generator.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.MaxStringLengthInjectingDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.manifest.JsonManifestWriter;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.utils.FileUtilsImpl;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Class to define default bindings for Guice injection. Utilises the generation config source to determine which
 * 'generate' classes should be bound for this execution run.
 */
public class GeneratorModule extends AbstractModule {
    private final GenerationConfigSource generationConfigSource;

    public GeneratorModule(GenerationConfigSource configSource) {
        this.generationConfigSource = configSource;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bind(GenerationConfigSource.class).toInstance(generationConfigSource);

        // Bind providers - used to retrieve implementations based on user input
        bind(DecisionTreeOptimiser.class).toProvider(DecisionTreeOptimiserProvider.class);
        bind(OutputFormat.class).toProvider(OutputFormatProvider.class);
        bind(TreePartitioner.class).toProvider(TreePartitioningProvider.class);
        bind(DecisionTreeWalker.class).toProvider(DecisionTreeWalkerProvider.class);
        bind(ProfileValidator.class).toProvider(ProfileValidatorProvider.class);
        bind(ReductiveDataGeneratorMonitor.class).toProvider(MonitorProvider.class).in(Singleton.class);
        bind(IterationVisualiser.class).toProvider(IterationVisualiserProvider.class);
        bind(RowSpecDataBagGenerator.class).toProvider(RowSpecDataBagSourceFactoryProvider.class);
        bind(CombinationStrategy.class).toProvider(CombinationStrategyProvider.class);
        bind(SingleDatasetOutputTarget.class).toProvider(SingleDatasetOutputTargetProvider.class);

        // bind config directly
        bind(DataGenerationType.class).toInstance(generationConfigSource.getGenerationType());
        bind(Path.class)
            .annotatedWith(Names.named("config:outputPath"))
            .toInstance(generationConfigSource.getOutputPath());
        bind(boolean.class)
            .annotatedWith(Names.named("config:canOverwriteOutputFiles"))
            .toInstance(generationConfigSource.overwriteOutputFiles());
        bind(long.class)
            .annotatedWith(Names.named("config:maxRows"))
            .toInstance(generationConfigSource.getMaxRows());
        bind(boolean.class)
            .annotatedWith(Names.named("config:tracingIsEnabled"))
            .toInstance(generationConfigSource.isEnableTracing());

        // Bind known implementations - no user input required
        bind(ManifestWriter.class).to(JsonManifestWriter.class);
        bind(DataGeneratorMonitor.class).to(ReductiveDataGeneratorMonitor.class);
        bind(DataGenerator.class).to(DecisionTreeDataGenerator.class);
        bind(DecisionTreeFactory.class).to(MaxStringLengthInjectingDecisionTreeFactory.class);
        bind(FieldValueSourceEvaluator.class).to(StandardFieldValueSourceEvaluator.class);
        bind(FileUtils.class).to(FileUtilsImpl.class);

        bind(VelocityMonitor.class)
            .toInstance(new VelocityMonitor(new PrintWriter(System.out, true)));
        bind(JavaUtilRandomNumberGenerator.class)
            .toInstance(new JavaUtilRandomNumberGenerator(OffsetDateTime.now().getNano()));
    }
}
