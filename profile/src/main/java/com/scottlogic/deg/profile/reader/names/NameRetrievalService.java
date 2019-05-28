package com.scottlogic.deg.profile.reader.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NameRetrievalService implements CatalogService<NameConstraintTypes, String> {

    @Override
    public Set<String> retrieveValues(NameConstraintTypes configuration) {
        switch (configuration) {
            case FIRST:
                return arrayToString("John", "Jon", "Jonny");
            case LAST:
                return arrayToString("Johnson", "Jonson", "Jones", "Joneson");
            default:
                throw new UnsupportedOperationException("Unsupported enum value " + configuration);
        }
    }

    private static Set<String> arrayToString(String... array) {
        return Stream.of(array).collect(Collectors.toSet());
    }

}
