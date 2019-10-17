package com.scottlogic.deg.generator.generation.visualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisualiserWriterFactoryTest {
    @TempDir
    File tempDir;

    @Test
    void create_validPath_expectSuccess() throws Exception {
        VisualiserWriterFactory writerFactory = new VisualiserWriterFactory(tempDir.toPath());
        Writer result = writerFactory.create("destination");
        assertNotNull(result);
        result.close();
    }

    @Test
    void create_invalidPath_expectFailure() {
        VisualiserWriterFactory writerFactory = new VisualiserWriterFactory(new File("madeUpBadPath").toPath());
        assertThrows(FileNotFoundException.class, () -> writerFactory.create("destination"));
    }
}
