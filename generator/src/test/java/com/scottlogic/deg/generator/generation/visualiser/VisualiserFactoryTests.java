package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.config.detail.VisualiserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class VisualiserFactoryTests {

    private VisualiserFactory factory;
    private VisualiserWriterFactory writerFactory;

    @BeforeEach
    void setUp() {
        writerFactory = mock(VisualiserWriterFactory.class);
    }

    @Test
    void add_VisualiserOff_always_expect_noop() throws Exception {
        constructFactory(VisualiserLevel.OFF);
        for (VisualiserLevel level : VisualiserLevel.values()) {
            Visualiser visualiser = callCreate(level);
            assertEquals(NoopVisualiser.class, visualiser.getClass());
        }
    }

    @Test
    void add_belowMinimumLevel_expect_noop() throws Exception {
        constructFactory(VisualiserLevel.STANDARD);
        Visualiser visualiser = callCreate(VisualiserLevel.DETAILED);
        assertEquals(NoopVisualiser.class, visualiser.getClass());
    }

    @Test
    void add_atMinimumLevel_expect_dot() throws Exception {
        constructFactory(VisualiserLevel.STANDARD);
        Visualiser visualiser = callCreate(VisualiserLevel.STANDARD);
        assertEquals(DotVisualiser.class, visualiser.getClass());
    }

    @Test
    void add_aboveMinimumLevel_expect_dot() throws Exception {
        constructFactory(VisualiserLevel.DETAILED);
        Visualiser visualiser = callCreate(VisualiserLevel.STANDARD);
        assertEquals(DotVisualiser.class, visualiser.getClass());
    }

    private Visualiser callCreate(VisualiserLevel level) throws Exception {
        return factory.create(level, "destination");
    }

    private void constructFactory(VisualiserLevel minimumLevel) {
        factory = new VisualiserFactory(minimumLevel, writerFactory);
    }

}
