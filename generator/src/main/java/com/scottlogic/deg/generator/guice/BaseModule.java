package com.scottlogic.deg.generator.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.commandline.GenerateCommandLine;
import com.scottlogic.deg.generator.commandline.VisualiseCommandLine;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualConstraintRuleViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualRuleProfileViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.ProfileViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.RuleViolator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.inputs.validation.reporters.SystemOutProfileValidationReporter;
import com.scottlogic.deg.generator.outputs.datasetwriters.DataSetWriter;
import com.scottlogic.deg.generator.outputs.manifest.JsonManifestWriter;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import com.scottlogic.deg.generator.visualisation.VisualisationConfigSource;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.routes.ExhaustiveProducer;
import com.scottlogic.deg.generator.walker.routes.RowSpecRouteProducer;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidator;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Class to define default bindings for Guice injection. Utilises the generation config source to determine which
 * 'generate' classes should be bound for this execution run.
 */
public class BaseModule extends AbstractModule {
    private final ConfigSource configSource;

    public BaseModule(ConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bind(GenerationConfigSource.class).to(GenerateCommandLine.class);
        bind(VisualisationConfigSource.class).to(VisualiseCommandLine.class);
        bindAllCommandLineTypes();

        // Bind providers - used to retrieve implementations based on user input
        bind(DecisionTreeOptimiser.class).toProvider(DecisionTreeOptimiserProvider.class);
        bind(DataSetWriter.class).toProvider(DataSetWriterProvider.class);
        bind(TreePartitioner.class).toProvider(TreePartitioningProvider.class);
        bind(DecisionTreeWalker.class).toProvider(DecisionTreeWalkerProvider.class);
        bind(ProfileValidator.class).toProvider(ProfileValidatorProvider.class);
        bind(GenerationEngine.class).toProvider(GenerationEngineProvider.class);
        bind(ReductiveDataGeneratorMonitor.class).toProvider(MonitorProvider.class).in(Singleton.class);
        bind(IterationVisualiser.class).toProvider(IterationVisualiserProvider.class);
        bind(RowSpecDataBagSourceFactory.class).toProvider(RowSpecDataBagSourceFactoryProvider.class);
        bind(ProfileSchemaValidator.class).toProvider(ProfileSchemaValidatorProvider.class);

        // Bind known implementations - no user input required
        bind(ManifestWriter.class).to(JsonManifestWriter.class);
        bind(DataGeneratorMonitor.class).to(ReductiveDataGeneratorMonitor.class);
        bind(DataGenerator.class).to(DecisionTreeDataGenerator.class);
        bind(DecisionTreeFactory.class).to(ProfileDecisionTreeFactory.class);
        bind(ProfileValidationReporter.class).to(SystemOutProfileValidationReporter.class);
        bind(RowSpecRouteProducer.class).to(ExhaustiveProducer.class);
        bind(ProfileReader.class).to(JsonProfileReader.class);
        bind(OutputTarget.class).to(FileOutputTarget.class);
        bind(FieldValueSourceEvaluator.class).to(StandardFieldValueSourceEvaluator.class);
        bind(ProfileViolator.class).to(IndividualRuleProfileViolator.class);
        bind(RuleViolator.class).to(IndividualConstraintRuleViolator.class);
        bind(ConfigValidator.class).to(GenerationConfigValidator.class);

        bind(new TypeLiteral<List<ViolationFilter>>() {
        }).toProvider(ViolationFiltersProvider.class);

        bind(Path.class).annotatedWith(Names.named("outputPath")).toProvider(OutputPathProvider.class);

        bind(VelocityMonitor.class).in(Singleton.class);
        bind(JavaUtilRandomNumberGenerator.class).toInstance(new JavaUtilRandomNumberGenerator(OffsetDateTime.now().getNano()));

    }

    private void bindAllCommandLineTypes() {
        if (this.configSource instanceof GenerateCommandLine) {
            bind(GenerateCommandLine.class).toInstance((GenerateCommandLine) this.configSource);
            bind(ConfigSource.class).to(GenerationConfigSource.class);
        } else if (this.configSource instanceof VisualiseCommandLine) {
            bind(VisualiseCommandLine.class).toInstance((VisualiseCommandLine) this.configSource);
            bind(ConfigSource.class).to(VisualisationConfigSource.class);
        }
    }
}
