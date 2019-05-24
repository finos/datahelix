package com.scottlogic.deg.output.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.manifest.JsonManifestWriter;
import com.scottlogic.deg.output.manifest.ManifestWriter;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.writer.OutputWriterFactory;

public class OutputModule extends AbstractModule {

    private final OutputConfigSource outputConfigSource;

    public OutputModule(OutputConfigSource outputConfigSource) {
        this.outputConfigSource = outputConfigSource;
    }

    @Override
    protected void configure() {
        bind(OutputConfigSource.class).toInstance(outputConfigSource);

        bind(OutputWriterFactory.class).toProvider(OutputWriterFactoryProvider.class);
        bind(SingleDatasetOutputTarget.class).toProvider(SingleDatasetOutputTargetProvider.class);
        bind(ManifestWriter.class).to(JsonManifestWriter.class);

        bind(OutputPath.class)
            .toInstance(OutputPath.of(outputConfigSource.getOutputPath()));
        bind(boolean.class)
            .annotatedWith(Names.named("config:canOverwriteOutputFiles"))
            .toInstance(outputConfigSource.overwriteOutputFiles());
    }
}
