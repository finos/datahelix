package com.scottlogic.deg.profile.reader.file.names;


import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NameRetrievalServiceTest {

    @InjectMocks
    private NameRetrievalService service;

    @Mock
    private Function<String, Set<String>> mapper;

    private void mockNames(String fileName, String... names) {
        InputStream mockStream = mock(InputStream.class);
        when(mapper.apply(fileName)).thenReturn(SetUtils.setOf(names));
    }

    @Test
    public void retrieveValuesFirst() {
        mockFirstNames();
        Set<String> names = service.retrieveValues(NameConstraintTypes.FIRST);
        assertEquals(names, SetUtils.setOf("Mark", "Paul", "Jolene", "Tanya"));
    }

    private void mockFirstNames() {
        mockNames("names/firstname_male.csv", "Mark", "Paul");
        mockNames("names/firstname_female.csv", "Jolene", "Tanya");
    }

    @Test
    public void retrieveValuesLast() {
        mockLastNames();
        Set<String> names = service.retrieveValues(NameConstraintTypes.LAST);
        assertEquals(names, SetUtils.setOf("Gore", "May"));
    }

    private void mockLastNames() {
        mockNames("names/surname.csv", "Gore", "May");
    }

    @Test
    public void retrieveValuesFull() {
        mockFirstNames();
        mockLastNames();
        Set<String> names = service.retrieveValues(NameConstraintTypes.FULL);
        assertEquals(names, SetUtils.setOf("Mark Gore", "Paul Gore", "Jolene Gore", "Tanya Gore",
            "Mark May", "Paul May", "Jolene May", "Tanya May"));
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        Set<String> result = service.retrieveValues(config);
        assertNotNull(result);
    }
}