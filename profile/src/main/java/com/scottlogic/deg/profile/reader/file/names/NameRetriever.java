package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.*;

public final class NameRetriever {

    private NameRetriever() {
        throw new UnsupportedOperationException("No static class instantiation");
    }

    public static Set<Object> loadNamesFromFile(NameConstraintTypes configuration) {
        Set<String> names;
        if (configuration == FULL) {
            names = combineFirstWithLastNames(
                generateNamesFromSingleFile(FIRST.getFilePath()),
                generateNamesFromSingleFile(LAST.getFilePath()));
        } else {
            names = generateNamesFromSingleFile(configuration.getFilePath());
        }
        return new HashSet<>(names);
    }

    private static Set<String> generateNamesFromSingleFile(String source) {
        InputStream stream = NameRetriever.class.getClassLoader().getResourceAsStream(source);
        Set<String> result = CsvInputStreamReader.retrieveLines(stream);
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private static Set<String> combineFirstWithLastNames(Set<String> firstNames, Set<String> lastNames) {
        Set<String> names = new HashSet<>();
        for (String first : firstNames) {
            for (String last : lastNames) {
                names.add(String.format("%s %s", first, last));
            }
        }
        return names;
    }

}
