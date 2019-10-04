package com.scottlogic.deg.generator.config.detail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisualiserLevelTests {
    @Test
    void sameOrHigherThan() {
        assertEquals(3, VisualiserLevel.values().length); // done so know to update test if more added

        assertTrue(VisualiserLevel.OFF.sameOrHigherThan(VisualiserLevel.OFF));
        assertFalse(VisualiserLevel.OFF.sameOrHigherThan(VisualiserLevel.STANDARD));
        assertFalse(VisualiserLevel.OFF.sameOrHigherThan(VisualiserLevel.DETAILED));

        assertTrue(VisualiserLevel.STANDARD.sameOrHigherThan(VisualiserLevel.OFF));
        assertTrue(VisualiserLevel.STANDARD.sameOrHigherThan(VisualiserLevel.STANDARD));
        assertFalse(VisualiserLevel.STANDARD.sameOrHigherThan(VisualiserLevel.DETAILED));

        assertTrue(VisualiserLevel.DETAILED.sameOrHigherThan(VisualiserLevel.OFF));
        assertTrue(VisualiserLevel.DETAILED.sameOrHigherThan(VisualiserLevel.STANDARD));
        assertTrue(VisualiserLevel.DETAILED.sameOrHigherThan(VisualiserLevel.DETAILED));
    }
}
