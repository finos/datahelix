package com.scottlogic.deg.generator.Guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.CommandLine.CanGenerate;
import com.scottlogic.deg.generator.CommandLine.CommandLineBase;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.NoOpIterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

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
        bind(TreePartitioner.class).toProvider(TreePartitioningProvider.class);
        bind(DecisionTreeOptimiser.class).toProvider(DecisionTreeOptimiserProvider.class);
        bind(GenerationConfig.class).toProvider(GenerationConfigProvider.class);
        bind(Profile.class).toProvider(ProfileProvider.class);
        bind(FixFieldStrategy.class).toProvider(FixFieldStrategyProvider.class);
        bind(DecisionTreeWalkerFactory.class).toProvider(DecisionTreeWalkerFactoryProvider.class);
        bind(FileOutputTarget.class).toProvider(FileOutputTargetProvider.class);
        bind(GenerationEngine.class).toProvider(GenerationEngineProvider.class);
        // Bind known implementations - no user input required
        bind(ReductiveDataGeneratorMonitor.class).to(NoopDataGeneratorMonitor.class);
        bind(IterationVisualiser.class).to(NoOpIterationVisualiser.class);

        // Bind varying providers - used for varying implmentations based on user input
        bind(DecisionTreeWalker.class).toProvider(DecisionTreeWalkerProvider.class);
    }

    private void bindAllCommandLineTypes() {
        if (this.commandLine instanceof GenerateCommandLine) {
            bind(GenerateCommandLine.class).toInstance((GenerateCommandLine) this.commandLine);
            bind(CanGenerate.class).to(GenerateCommandLine.class);
        }
//        TODO: Apply visualise, generate test cases
//        if (this.commandLine instanceof VisualiseCommandLine) {
//            bind(VisualiseCommandLine.class).toInstance((VisualiseCommandLine) this.commandLine);
//        }
    }
}
