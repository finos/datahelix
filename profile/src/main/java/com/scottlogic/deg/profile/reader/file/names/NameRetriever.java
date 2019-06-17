package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.profile.reader.file.CSVFromPathToStringsLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.FIRST;
import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.LAST;

public class NameRetriever {

    public Set<Object> loadNamesFromFile(NameConstraintTypes configuration) {
        Set<String> names;
        switch (configuration) {
            case FIRST:
            case LAST:
                names = generateSingles(configuration.getFilePath());
                break;
            case FULL:
                names = generateCombinations(
                    generateSingles(FIRST.getFilePath()),
                    generateSingles(LAST.getFilePath()));
                break;
            default:
                throw new UnsupportedOperationException("Name not implemented of type: " + configuration);
        }
        return new HashSet<>(names);
    }

    private Set<String> generateSingles(String source) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(source);
        Set<String> result = CSVFromPathToStringsLoader.retrieveNames(stream);
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private static Set<String> generateCombinations(Set<String> firstNames, Set<String> lastNames) {
        Set<String> names = new HashSet<>();
        for (String first : firstNames) {
            for (String last : lastNames) {
                names.add(combineFirstAndLastName(first, last));
            }
        }
        return names;
    }

    private static String combineFirstAndLastName(final String first, final String last) {
        return first + " " + last;
    }

}
