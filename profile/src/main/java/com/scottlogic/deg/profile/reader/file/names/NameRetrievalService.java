package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.FIRST;
import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.LAST;

public class NameRetrievalService {

    private static final String SEP = "/";

    private static final String NAMES_ROOT = "names" + SEP;

    private static final String FIRST_MALE_NAMES = NAMES_ROOT + "firstname_male.csv";

    private static final String FIRST_FEMALE_NAMES = NAMES_ROOT + "firstname_female.csv";

    private static final String LAST_NAMES = NAMES_ROOT + "surname.csv";

    private final Map<NameConstraintTypes, Set<String>> nameTypeMappings;

    private final Function<String, Set<NameHolder>> mapper;

    public NameRetrievalService(final Function<String, Set<NameHolder>> mapper) {
        this.mapper = mapper;
        this.nameTypeMappings = setupNameMappings();
    }

    private Map<NameConstraintTypes, Set<String>> setupNameMappings() {
        Map<NameConstraintTypes, Set<String>> mappings = new EnumMap<>(NameConstraintTypes.class);
        mappings.put(LAST, Stream.of(LAST_NAMES).collect(Collectors.toSet()));
        mappings.put(FIRST, Stream.of(FIRST_MALE_NAMES, FIRST_FEMALE_NAMES).collect(Collectors.toSet()));
        return mappings;
    }

    public Set<NameHolder> retrieveValues(NameConstraintTypes configuration) {
        switch (configuration) {
            case FIRST:
            case LAST:
                return generateSingles(nameTypeMappings.get(configuration));
            case FULL:
                return generateCombinations(generateSingles(nameTypeMappings.get(FIRST)),
                    generateSingles(nameTypeMappings.get(LAST)));
            default:
                throw new UnsupportedOperationException("Name not implemented of type: " + configuration);
        }
    }

    private Set<NameHolder> generateSingles(Set<String> sources) {
        return sources.stream()
            .map(mapper)
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
