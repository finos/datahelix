package com.scottlogic.deg.generator.Guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.scottlogic.deg.generator.CommandLine.CommandLineBase;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.CommandLine.GenerateTestCasesCommandLine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.inputs.validation.reporters.SystemOutProfileValidationReporter;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.walker.*;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.NoOpIterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.HierarchicalDependencyFixFieldStrategy;
import com.scottlogic.deg.generator.walker.routes.ExhaustiveProducer;
import com.scottlogic.deg.generator.walker.routes.RowSpecRouteProducer;

import java.nio.file.Path;

public class IoCContainer extends AbstractModule {
    private final CommandLineBase commandLine;

    public IoCContainer(CommandLineBase commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bindAllCommandLineTypes();

        // Bind providers - used to retrieve implementations based on user input
        bind(DecisionTreeOptimiser.class).toProvider(DecisionTreeOptimiserProvider.class);
        bind(Profile.class).toProvider(ProfileProvider.class);
        bind(DataSetWriter.class).toProvider(DataSetWriterProvider.class);
        bind(TreePartitioner.class).toProvider(TreePartitioningProvider.class);
        bind(DecisionTreeWalker.class).toProvider(DecisionTreeWalkerProvider.class);
        bind(ProfileValidator.class).toProvider(ProfileValidatorProvider.class);

        // Bind known implementations - no user input required
        bind(DataGeneratorMonitor.class).to(ReductiveDataGeneratorMonitor.class);
        bind(ReductiveDataGeneratorMonitor.class).to(NoopDataGeneratorMonitor.class);
        bind(IterationVisualiser.class).to(NoOpIterationVisualiser.class);
        bind(FixFieldStrategy.class).to(HierarchicalDependencyFixFieldStrategy.class);
        bind(DataGenerator.class).to(DecisionTreeDataGenerator.class);
        bind(DecisionTreeWalkerFactory.class).to(RuntimeDecisionTreeWalkerFactory.class);
        bind(DecisionTreeFactory.class).to(ProfileDecisionTreeFactory.class);
        bind(ProfileValidationReporter.class).to(SystemOutProfileValidationReporter.class);
        bind(RowSpecRouteProducer.class).to(ExhaustiveProducer.class);
        bind(ProfileReader.class).to(JsonProfileReader.class);
        bind(OutputTarget.class).to(FileOutputTarget.class);

        bind(DecisionTreeWalker.class).annotatedWith(Names.named("cartesian")).to(CartesianProductDecisionTreeWalker.class);
        bind(DecisionTreeWalker.class).annotatedWith(Names.named("reductive")).to(ReductiveDecisionTreeWalker.class);
        bind(DecisionTreeWalker.class).annotatedWith(Names.named("routed")).to(DecisionTreeRoutesTreeWalker.class);

        bind(Path.class).annotatedWith(Names.named("outputPath")).toProvider(OutputPathProvider.class);
    }

    private void bindAllCommandLineTypes() {
        if (this.commandLine instanceof GenerateCommandLine) {
            bind(GenerateCommandLine.class).toInstance((GenerateCommandLine) this.commandLine);
            bind(GenerationConfigSource.class).to(GenerateCommandLine.class);
        }

        if (this.commandLine instanceof GenerateTestCasesCommandLine) {
            bind(GenerateTestCasesCommandLine.class).toInstance((GenerateTestCasesCommandLine) this.commandLine);
            bind(GenerationConfigSource.class).to(GenerateTestCasesCommandLine.class);
        }
    }
}
