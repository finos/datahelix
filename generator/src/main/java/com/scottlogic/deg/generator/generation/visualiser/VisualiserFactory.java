package com.scottlogic.deg.generator.generation.visualiser;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.config.detail.VisualiserLevel;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

import java.io.IOException;
import java.io.Writer;

public class VisualiserFactory {

    private final VisualiserLevel minimumLevel;
    private final VisualiserWriterFactory writerFactory;

    @Inject
    public VisualiserFactory(GenerationConfigSource config) {
        this(config.getVisualiserLevel(), new VisualiserWriterFactory(config.getVisualiserOutputFolder()));
    }

    VisualiserFactory(VisualiserLevel minimumLevel, VisualiserWriterFactory writerFactory) {
        this.minimumLevel = minimumLevel;
        this.writerFactory = writerFactory;
    }

    public Visualiser create(VisualiserLevel level, String destination) {
        if (minimumLevel != VisualiserLevel.OFF && minimumLevel.sameOrMoreVerboseThan(level)) {
            System.err.println("CREATING VISUALISER with level=" + level + ", destination=" + destination);
            Writer writer = createWriter(destination);
            DecisionTreeVisualisationWriter decisionTreeVisualisationWriter = new DecisionTreeVisualisationWriter(writer);
            return new DotVisualiser(decisionTreeVisualisationWriter);
        } else {
            return new NoopVisualiser();
        }
    }

    private Writer createWriter(String destination) {
        try {
            return writerFactory.create(destination);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
