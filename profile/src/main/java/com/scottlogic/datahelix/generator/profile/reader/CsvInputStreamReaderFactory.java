package com.scottlogic.datahelix.generator.profile.reader;

import com.google.inject.name.Named;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;

public class CsvInputStreamReaderFactory {
    private final String filePath;

    @Inject
    public CsvInputStreamReaderFactory(@Named("config:filePath") String filePath) {
        this.filePath = filePath.endsWith(File.separator) || filePath.isEmpty()
            ? filePath
            : filePath + File.separator;
    }

    public CsvInputReader getReaderForFile(String file) {
        return new CsvFileInputReader(filePath + file);
    }

    public CsvInputReader getReaderForStream(InputStream stream, String name) {
        return new CsvStreamInputReader(stream, name);
    }
}
