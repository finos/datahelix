package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.config.detail.VisualiserLevel;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;

import java.io.IOException;
import java.io.Writer;

public class VisualiserFactory {

    private final VisualiserLevel minimumLevel;
    private final VisualiserWriterFactory writerFactory;

    public VisualiserFactory(VisualiserLevel minimumLevel, VisualiserWriterFactory writerFactory) {
        this.minimumLevel = minimumLevel;
        this.writerFactory = writerFactory;
    }

    public Visualiser create(VisualiserLevel level, String destination) throws IOException {
        if (minimumLevel != VisualiserLevel.OFF && minimumLevel.sameOrHigherThan(level)) {
            System.err.println("CREATING VISUALISER with level=" + level + ", destination=" + destination);
            Writer writer = writerFactory.create(destination);
            DecisionTreeVisualisationWriter decisionTreeVisualisationWriter = new DecisionTreeVisualisationWriter(writer);
            return new DotVisualiser(decisionTreeVisualisationWriter);
        } else {
            return new NoopVisualiser();
        }
    }
}
