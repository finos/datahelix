package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.generator.utils.SetUtils;
import com.scottlogic.deg.profile.reader.file.CSVPathSetMapper;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.FIRST;
import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.LAST;

public class NameRetrievalService {

    private static final Map<NameConstraintTypes, Set<String>> NAME_TYPE_MAPPINGS = setupNameMappings();

    private final Function<String, Set<String>> filepathToNames;

    public NameRetrievalService(final Function<String, InputStream> pathMapper) {
        filepathToNames = new CSVPathSetMapper(pathMapper);
    }

    private static Map<NameConstraintTypes, Set<String>> setupNameMappings() {
        Map<NameConstraintTypes, Set<String>> mappings = new EnumMap<>(NameConstraintTypes.class);
        mappings.put(LAST, NameClassPathFiles.getLastNames());
        mappings.put(FIRST, NameClassPathFiles.getFirstNames());
        return mappings;
    }

    public Set<String> retrieveValues(NameConstraintTypes configuration) {
        switch (configuration) {
            case FIRST:
            case LAST:
                return generateSingles(NAME_TYPE_MAPPINGS.get(configuration));
            case FULL:
                return generateCombinations(generateSingles(NAME_TYPE_MAPPINGS.get(FIRST)),
                    generateSingles(NAME_TYPE_MAPPINGS.get(LAST)));
            default:
                throw new UnsupportedOperationException("Name not implemented of type: " + configuration);
        }
    }

    private Set<String> generateSingles(Set<String> sources) {
        return sources.stream()
            .map(filepathToNames)
            .reduce(new HashSet<>(), SetUtils::populateSet);
    }

    private Set<String> generateCombinations(Set<String> firstNames,
                                                 Set<String> lastNames) {
        return firstNames.stream()
            .flatMap(first -> lastNames.stream()
                .map(last -> combineFirstAndLastName(first, last)))
            .collect(Collectors.toSet());
    }

    private String combineFirstAndLastName(final String first,
                                               final String last) {
        return first + " " + last;
    }

}
