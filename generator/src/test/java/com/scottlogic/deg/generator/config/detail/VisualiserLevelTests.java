package com.scottlogic.deg.generator.config.detail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisualiserLevelTests {
    @Test
    void sameOrHigherThan() {
        assertEquals(3, VisualiserLevel.values().length); // done so know to update test if more added

        assertTrue(VisualiserLevel.OFF.sameOrMoreVerboseThan(VisualiserLevel.OFF));
        assertFalse(VisualiserLevel.OFF.sameOrMoreVerboseThan(VisualiserLevel.STANDARD));
        assertFalse(VisualiserLevel.OFF.sameOrMoreVerboseThan(VisualiserLevel.DETAILED));

        assertTrue(VisualiserLevel.STANDARD.sameOrMoreVerboseThan(VisualiserLevel.OFF));
        assertTrue(VisualiserLevel.STANDARD.sameOrMoreVerboseThan(VisualiserLevel.STANDARD));
        assertFalse(VisualiserLevel.STANDARD.sameOrMoreVerboseThan(VisualiserLevel.DETAILED));

        assertTrue(VisualiserLevel.DETAILED.sameOrMoreVerboseThan(VisualiserLevel.OFF));
        assertTrue(VisualiserLevel.DETAILED.sameOrMoreVerboseThan(VisualiserLevel.STANDARD));
        assertTrue(VisualiserLevel.DETAILED.sameOrMoreVerboseThan(VisualiserLevel.DETAILED));
    }
}
