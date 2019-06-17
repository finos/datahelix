package com.scottlogic.deg.profile.reader.file.names;


import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NameRetrieverTest {

    private NameRetriever service = new NameRetriever();

    private void mockNames(String fileName, String... names) {
        String combinedName = Stream.of(names)
            .reduce((a, b) -> String.join("\n", a, b))
            .orElseThrow(IllegalStateException::new);
    }

    private void mockFirstNames() {
        mockNames("names/firstname.csv", "Mark", "Paul", "Jolene", "Tanya");
    }

    @Test
    public void retrieveValuesFirst() {
        mockFirstNames();

        Set<Object> names = setFromConstraint(NameConstraintTypes.FIRST);

        assertEquals(704, names.size());
    }

    private void mockLastNames() {
        mockNames("names/surname.csv", "Gore", "May");
    }

    @Test
    public void retrieveValuesLast() {
        mockLastNames();

        Set<Object> names = setFromConstraint(NameConstraintTypes.LAST);

        assertEquals(280, names.size());
    }

    private void mockAllNames() {
        mockFirstNames();
        mockLastNames();
    }

    @Test
    public void retrieveValuesFull() {
        mockAllNames();

        Set<Object> names = setFromConstraint(NameConstraintTypes.FULL);

        assertEquals(197120, names.size());
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        switch (config) {
            case FIRST:
                mockFirstNames();
                break;
            case LAST:
                mockLastNames();
                break;
            case FULL:
                mockAllNames();
                break;
        }

        Set<Object> result = setFromConstraint(config);

        assertNotNull(result);
    }

    private Set<Object> setFromConstraint(NameConstraintTypes config) {
        return service.loadNamesFromFile(config);
    }
}