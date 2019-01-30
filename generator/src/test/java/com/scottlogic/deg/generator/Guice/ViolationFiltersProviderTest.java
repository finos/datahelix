package com.scottlogic.deg.generator.Guice;

import com.scottlogic.deg.generator.constraints.atomic.StringHasLengthConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.violations.filters.ConstraintTypeViolationFilter;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class ViolationFiltersProviderTest {

    @Test
    void whenNullConstraintsToViolate_ReturnEmptyListOfViolationFilters() {
        GenerationConfigSource configSource = mock(GenerationConfigSource.class);
        when(configSource.getConstraintsToNotViolate()).thenReturn(null);
        ViolationFiltersProvider provider = new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        assertThat(provider.get(), is(empty()));
    }

    @Test
    void whenEmptyConstraintsToViolate_ReturnEmptyListOfViolationFilters() {
        GenerationConfigSource configSource = mock(GenerationConfigSource.class);
        when(configSource.getConstraintsToNotViolate()).thenReturn(Collections.emptyList());
        ViolationFiltersProvider provider = new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        assertThat(provider.get(), is(empty()));
    }

    @Test
    void hasLengthConstraintsToViolate_ReturnListWithOneHasLengthFilter() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        GenerationConfigSource configSource = mock(GenerationConfigSource.class);
        when(configSource.getConstraintsToNotViolate()).thenReturn(Arrays.asList(AtomicConstraintType.HASLENGTH));
        ViolationFiltersProvider provider = new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        List<ViolationFilter> filters = provider.get();
        assertThat(filters, hasSize(1));
        assertThat(filters.get(0), instanceOf(ConstraintTypeViolationFilter.class));

        Class reflection = Class.forName("com.scottlogic.deg.generator.violations.filters.ConstraintTypeViolationFilter");
        Field field = reflection.getDeclaredField("constraintType");
        field.setAccessible(true);

        ConstraintTypeViolationFilter filter = (ConstraintTypeViolationFilter) filters.get(0);
        assertThat(field.get(filter), equalTo(StringHasLengthConstraint.class));
    }

    @Test
    void twoConstraintsToViolate_ReturnListWithTwoFilter() {
        GenerationConfigSource configSource = mock(GenerationConfigSource.class);
        when(configSource.getConstraintsToNotViolate()).thenReturn(Arrays.asList(AtomicConstraintType.HASLENGTH, AtomicConstraintType.ISINSET));
        ViolationFiltersProvider provider = new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        assertThat(provider.get(), hasSize(2));
    }
}