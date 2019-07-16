package com.scottlogic.deg.generator.fieldspecs;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldSpecMergerTests {
    private FieldSpecMerger merger = new FieldSpecMerger();

    @Test
    void merge_withContradictingLeftFieldSpec_returnsContradiction() {
        //Arrange
        FieldSpec contradictingFieldSpec = Mockito.mock(FieldSpec.class);
        FieldSpec unContradictingFieldSpec = Mockito.mock(FieldSpec.class);

        Mockito.when(contradictingFieldSpec.isContradictory()).thenReturn(true);
        Mockito.when(unContradictingFieldSpec.isContradictory()).thenReturn(false);

        Mockito.when(unContradictingFieldSpec.getWhitelist()).thenReturn(null);
        Mockito.when(contradictingFieldSpec.getWhitelist()).thenReturn(null);

        //Act
        Optional<FieldSpec> actual = merger.merge(contradictingFieldSpec, unContradictingFieldSpec);

        //Assert
        assertFalse(actual.isPresent());
    }

    @Test
    void merge_withContradictingRightFieldSpec_returnsContradiction() {
        //Arrange
        FieldSpec contradictingFieldSpec = Mockito.mock(FieldSpec.class);
        FieldSpec unContradictingFieldSpec = Mockito.mock(FieldSpec.class);

        Mockito.when(contradictingFieldSpec.isContradictory()).thenReturn(true);
        Mockito.when(unContradictingFieldSpec.isContradictory()).thenReturn(false);

        Mockito.when(unContradictingFieldSpec.getWhitelist()).thenReturn(null);
        Mockito.when(contradictingFieldSpec.getWhitelist()).thenReturn(null);

        //Act
        Optional<FieldSpec> actual = merger.merge(unContradictingFieldSpec, contradictingFieldSpec);

        //Assert
        assertFalse(actual.isPresent());
    }

    @Test
    void merge_withUnContradictingFieldSpecs_returnsNoContradiction() {
        //Arrange
        FieldSpec contradictingFieldSpec = Mockito.mock(FieldSpec.class);
        FieldSpec unContradictingFieldSpec = Mockito.mock(FieldSpec.class);

        Mockito.when(contradictingFieldSpec.isContradictory()).thenReturn(false);
        Mockito.when(unContradictingFieldSpec.isContradictory()).thenReturn(false);

        Mockito.when(unContradictingFieldSpec.getWhitelist()).thenReturn(null);
        Mockito.when(contradictingFieldSpec.getWhitelist()).thenReturn(null);

        //Act
        Optional<FieldSpec> actual = merger.merge(unContradictingFieldSpec, contradictingFieldSpec);

        //Assert
        assertTrue(actual.isPresent());
    }
}