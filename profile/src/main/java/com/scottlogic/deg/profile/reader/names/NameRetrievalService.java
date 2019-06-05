package com.scottlogic.deg.profile.reader.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.profile.reader.CatalogService;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.FIRST;
import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.LAST;

public class NameRetrievalService implements CatalogService<NameConstraintTypes, NameFrequencyHolder> {

    private static final String SEP = "/";

    private static final String NAMES_ROOT = "names" + SEP;

    private static final String FIRST_MALE_NAMES = NAMES_ROOT + "firstname_male.csv";

    private static final String FIRST_FEMALE_NAMES = NAMES_ROOT + "firstname_female.csv";

    private static final String LAST_NAMES = NAMES_ROOT + "surname.csv";

    private static final Map<NameConstraintTypes, Set<String>> NAME_TYPE_MAPPINGS;

    private static final NamePopulator<InputStream> POPULATOR = new NameCSVPopulator();

    static {
        NAME_TYPE_MAPPINGS = new EnumMap<>(NameConstraintTypes.class);
        NAME_TYPE_MAPPINGS.put(LAST, Stream.of(LAST_NAMES).collect(Collectors.toSet()));
        NAME_TYPE_MAPPINGS.put(FIRST, Stream.of(FIRST_MALE_NAMES, FIRST_FEMALE_NAMES).collect(Collectors.toSet()));
    }

    @Override
    public Set<NameFrequencyHolder> retrieveValues(NameConstraintTypes configuration) {
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

    private Set<NameFrequencyHolder> generateSingles(Set<String> sources) {
        return sources.stream()
            .map(this::pathFromClasspath)
            .map(this::parseFromFile)
            .reduce(new HashSet<>(), this::populateSet);
    }

    private Set<NameFrequencyHolder> generateCombinations(Set<NameFrequencyHolder> firstNames,
                                                          Set<NameFrequencyHolder> lastNames) {
        return firstNames.stream()
            .flatMap(
                first -> lastNames.stream()
                    .map(last -> new NameFrequencyHolder(first.getName() + " " + last.getName(),
                        first.getFrequency() * last.getFrequency())))
            .collect(Collectors.toSet());
    }

    private InputStream pathFromClasspath(String classPath) {
        return Optional.ofNullable(this.getClass()
            .getClassLoader()
            .getResourceAsStream(classPath)
        ).orElseThrow(() -> new IllegalArgumentException("Path not found on classpath."));
    }


    private Set<NameFrequencyHolder> parseFromFile(InputStream path) {
        return POPULATOR.retrieveNames(path);
    }

    private Set<NameFrequencyHolder> populateSet(Set<NameFrequencyHolder> a, Set<NameFrequencyHolder> b) {
        a.addAll(b);
        return a;
    }

}
