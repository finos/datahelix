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
        Set<Object> names = NameRetriever.loadNamesFromFile(NameConstraintTypes.FIRST);

        assertEquals(704, names.size());
    }

    @Test
    public void retrieveValuesLast() {
        Set<Object> names = NameRetriever.loadNamesFromFile(NameConstraintTypes.LAST);

        assertEquals(280, names.size());
    }

    @Test
    public void retrieveValuesFull() {
        Set<Object> names = NameRetriever.loadNamesFromFile(NameConstraintTypes.FULL);

        assertEquals(197120, names.size());
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        Set<Object> result = NameRetriever.loadNamesFromFile(config);

        assertNotNull(result);
    }
}