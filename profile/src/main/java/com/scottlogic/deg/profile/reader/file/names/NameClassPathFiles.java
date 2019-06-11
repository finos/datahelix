package com.scottlogic.deg.profile.reader.file.names;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class NameClassPathFiles {

    private static final String SEP = "/";

    private static final String NAMES_ROOT = "names" + SEP;

    private static final String FIRST_MALE_NAMES = NAMES_ROOT + "firstname_male.csv";

    private static final String FIRST_FEMALE_NAMES = NAMES_ROOT + "firstname_female.csv";

    private static final String LAST_NAMES = NAMES_ROOT + "surname.csv";

    private NameClassPathFiles() {
        throw new UnsupportedOperationException("Should not instantiate utility class");
    }

    public static Set<String> getFirstNames() {
        return Stream.of(FIRST_MALE_NAMES, FIRST_FEMALE_NAMES).collect(Collectors.toSet());
    }

    public static Set<String> getLastNames() {
        return Stream.of(LAST_NAMES).collect(Collectors.toSet());
    }
}
