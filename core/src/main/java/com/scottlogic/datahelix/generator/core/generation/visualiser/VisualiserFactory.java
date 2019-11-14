/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.datahelix.generator.core.generation.visualiser;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.core.config.detail.VisualiserLevel;
import com.scottlogic.datahelix.generator.core.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.datahelix.generator.core.generation.GenerationConfigSource;

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
