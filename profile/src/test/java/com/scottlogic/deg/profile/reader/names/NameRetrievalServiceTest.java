package com.scottlogic.deg.profile.reader.names;


import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NameRetrievalServiceTest {

    @InjectMocks
    private NameRetrievalService service;

    @Mock
    private NamePopulator<String> populator;

    private void mockFirst() {
        mockNames("names/firstname_male.csv", "Mark", "Paul");
        mockNames("names/firstname_female.csv", "Jolene", "Tanya");
    }

    private void mockLast() {
        mockNames("names/surname.csv", "Gore", "May");
    }

    private void mockNames(String fileName, String... names) {
        when(populator.retrieveNames(fileName)).thenReturn(namesToHolders(names));
    }

    private Set<NameHolder> namesToHolders(String... names) {
        return Stream.of(names)
            .map(NameHolder::new)
            .collect(Collectors.toSet());
    }

    @Test
    public void retrieveValuesFirst() {
        mockFirst();
        Set<NameHolder> names = service.retrieveValues(NameConstraintTypes.FIRST);
        assertEquals(names, namesToHolders("Mark", "Paul", "Jolene", "Tanya"));
    }

    @Test
    public void retrieveValuesLast() {
        mockLast();
        Set<NameHolder> names = service.retrieveValues(NameConstraintTypes.LAST);
        assertEquals(names, namesToHolders("Gore", "May"));
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        Set<NameHolder> result = service.retrieveValues(config);
        assertNotNull(result);
    }
}