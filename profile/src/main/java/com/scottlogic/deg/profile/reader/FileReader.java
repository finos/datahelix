package com.scottlogic.deg.profile.reader;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;

import java.io.*;
import java.util.stream.Collectors;

public class FileReader {
    private final String filePath;

    @Inject
    public FileReader(@Named("config:filePath") String filePath) {
        this.filePath = filePath.endsWith(File.separator) || filePath.isEmpty()
            ? filePath
            : filePath + File.separator;
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

    public DistributedList<String> listFromMapFile(String file, String key) {
        InputStream streamFromPath = createStreamFromPath(appendPath(file));

        DistributedList<String> names = CsvInputStreamReader.retrieveLines(streamFromPath, key);
        closeStream(streamFromPath);

        return new DistributedList<>(
            names.distributedList().stream()
                .map(holder -> new WeightedElement<>(holder.element(), holder.weight()))
                .collect(Collectors.toList()));
    }

    private String appendPath(String path) {
        return filePath + path;
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
