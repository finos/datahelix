package com.scottlogic.deg.profile.reader.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.profile.reader.CatalogService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.FIRST;
import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.LAST;

public class NameRetrievalService implements CatalogService<NameConstraintTypes, NameHolder> {

    private static final String SEP = "/";

    private static final String NAMES_ROOT = "names" + SEP;

    private static final String FIRST_MALE_NAMES = NAMES_ROOT + "firstname_male.csv";

    private static final String FIRST_FEMALE_NAMES = NAMES_ROOT + "firstname_female.csv";

    private static final String LAST_NAMES = NAMES_ROOT + "surname.csv";

    private static final Map<NameConstraintTypes, Set<String>> NAME_TYPE_MAPPINGS;

    private final NamePopulator<String> populator;

    static {
        NAME_TYPE_MAPPINGS = new EnumMap<>(NameConstraintTypes.class);
        NAME_TYPE_MAPPINGS.put(LAST, Stream.of(LAST_NAMES).collect(Collectors.toSet()));
        NAME_TYPE_MAPPINGS.put(FIRST, Stream.of(FIRST_MALE_NAMES, FIRST_FEMALE_NAMES).collect(Collectors.toSet()));
    }

    public NameRetrievalService(final NamePopulator<String> populator) {
        this.populator = populator;
    }

    @Override
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
            .map(populator::retrieveNames)
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
        a.addAll(b);
        return a;
    }

}
