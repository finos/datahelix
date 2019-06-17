package com.scottlogic.deg.profile.reader.file.names;


import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NameRetrieverTest {

    @Test
    public void retrieveValuesFirst() {
        Set<Object> names = setFromConstraint(NameConstraintTypes.FIRST);

        assertEquals(704, names.size());
    }

    @Test
    public void retrieveValuesLast() {
        Set<Object> names = setFromConstraint(NameConstraintTypes.LAST);

        assertEquals(280, names.size());
    }

    @Test
    public void retrieveValuesFull() {
        Set<Object> names = setFromConstraint(NameConstraintTypes.FULL);

        assertEquals(197120, names.size());
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        Set<Object> result = setFromConstraint(config);

        assertNotNull(result);
    }

    private Set<Object> setFromConstraint(NameConstraintTypes config) {
        return NameRetriever.loadNamesFromFile(config);
    }
}