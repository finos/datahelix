package com.scottlogic.deg.profile.reader.atomic;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;

import java.io.*;
import java.util.stream.Collectors;

public class FromFileReader {
    private final String fromFilePath;

    @Inject
    public FromFileReader(@Named("config:fromFilePath") String fromFilePath) {
        if (fromFilePath.endsWith(File.separator) || fromFilePath.isEmpty()) {
            this.fromFilePath = fromFilePath;
        } else {
            this.fromFilePath = fromFilePath + File.separator;
        }
    }

    public DistributedList<Object> setFromFile(String file) {
        InputStream streamFromPath = createStreamFromPath(appendPath(file));

        DistributedList<String> names = CsvInputStreamReader.retrieveLines(streamFromPath);
        closeStream(streamFromPath);

        return new DistributedList<>(
            names.distributedList().stream()
                .map(holder -> new WeightedElement<>((Object) holder.element(), holder.weight()))
                .distinct()
                .collect(Collectors.toList()));
    }

    private String appendPath(String path) {
        return fromFilePath + path;
    }

    private static InputStream createStreamFromPath(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private static void closeStream(InputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
