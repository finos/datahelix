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

import com.scottlogic.datahelix.generator.core.config.detail.VisualiserLevel;
import com.scottlogic.datahelix.generator.core.generation.GenerationConfigSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VisualiserFactoryTests {
    private VisualiserFactory factory;
    private VisualiserWriterFactory writerFactory;

    @BeforeEach
    void setUp() {
        writerFactory = mock(VisualiserWriterFactory.class);
    }

    @Test
    void injectable_constructor() {
        GenerationConfigSource configSource = mock(GenerationConfigSource.class);
        when(configSource.getVisualiserLevel()).thenReturn(VisualiserLevel.STANDARD);
        when(configSource.getVisualiserOutputFolder()).thenReturn(new File(".").toPath());
        factory = new VisualiserFactory(configSource);
        assertNotNull(factory);
    }

    @Test
    void add_VisualiserOff_always_expect_noop() {
        constructFactory(VisualiserLevel.OFF);
        for (VisualiserLevel level : VisualiserLevel.values()) {
            Visualiser visualiser = callCreate(level);
            assertEquals(NoopVisualiser.class, visualiser.getClass());
        }
    }

    @Test
    void add_belowMinimumLevel_expect_noop() {
        constructFactory(VisualiserLevel.STANDARD);
        Visualiser visualiser = callCreate(VisualiserLevel.DETAILED);
        assertEquals(NoopVisualiser.class, visualiser.getClass());
    }

    @Test
    void add_atMinimumLevel_expect_dot() {
        constructFactory(VisualiserLevel.STANDARD);
        Visualiser visualiser = callCreate(VisualiserLevel.STANDARD);
        assertEquals(DotVisualiser.class, visualiser.getClass());
    }

    @Test
    void add_aboveMinimumLevel_expect_dot() {
        constructFactory(VisualiserLevel.DETAILED);
        Visualiser visualiser = callCreate(VisualiserLevel.STANDARD);
        assertEquals(DotVisualiser.class, visualiser.getClass());
    }

    private Visualiser callCreate(VisualiserLevel level) {
        return factory.create(level, "destination");
    }

    private void constructFactory(VisualiserLevel minimumLevel) {
        factory = new VisualiserFactory(minimumLevel, writerFactory);
    }

}
