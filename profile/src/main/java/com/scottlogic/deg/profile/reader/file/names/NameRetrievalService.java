package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;

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

    private final Function<String, Set<NameHolder>> filepathToNames;

    public NameRetrievalService(final Function<String, Set<NameHolder>> filepathToNames) {
        this.filepathToNames = filepathToNames;
    }

    private static Map<NameConstraintTypes, Set<String>> setupNameMappings() {
        Map<NameConstraintTypes, Set<String>> mappings = new EnumMap<>(NameConstraintTypes.class);
        mappings.put(LAST, NameClassPathFiles.getLastNames());
        mappings.put(FIRST, NameClassPathFiles.getFirstNames());
        return mappings;
    }

    public Set<NameHolder> retrieveValues(NameConstraintTypes configuration) {
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

    private Set<NameHolder> generateSingles(Set<String> sources) {
        return sources.stream()
            .map(filepathToNames)
            .reduce(new HashSet<>(), this::populateSet);
    }

    private Set<NameHolder> generateCombinations(Set<NameHolder> firstNames,
                                                 Set<NameHolder> lastNames) {
        return firstNames.stream()
            .flatMap(first -> lastNames.stream()
                .map(last -> combineFirstAndLastName(first, last)))
            .collect(Collectors.toSet());
    }

    private NameHolder combineFirstAndLastName(final NameHolder first,
                                               final NameHolder last) {
        final String name = first.getName() + " " + last.getName();
        return new NameHolder(name);
    }

    private Set<NameHolder> populateSet(Set<NameHolder> a, Set<NameHolder> b) {
        if (b != null) {
            a.addAll(b);
        }
        return a;
    }

}
