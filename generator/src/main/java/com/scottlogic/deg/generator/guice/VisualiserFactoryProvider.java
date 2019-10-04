package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.visualiser.VisualiserFactory;
import com.scottlogic.deg.generator.generation.visualiser.VisualiserWriterFactory;

public class VisualiserFactoryProvider implements Provider<VisualiserFactory> {

    private final GenerationConfigSource config;

    @Inject
    public VisualiserFactoryProvider(GenerationConfigSource config){
        this.config = config;
    }

    @Override
    public VisualiserFactory get() {
        VisualiserWriterFactory writerFactory = new VisualiserWriterFactory(config.getVisualiserOutputFolder());
        return new VisualiserFactory(config.getVisualiserLevel(), writerFactory);
    }
}
