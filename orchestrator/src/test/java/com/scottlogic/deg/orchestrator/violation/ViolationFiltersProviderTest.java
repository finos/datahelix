package com.scottlogic.deg.orchestrator.violation;

import com.scottlogic.deg.common.profile.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.StringHasLengthConstraint;
import com.scottlogic.deg.orchestrator.violate.AtomicConstraintTypeMapper;
import com.scottlogic.deg.generator.violations.filters.ConstraintTypeViolationFilter;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import com.scottlogic.deg.orchestrator.violate.ViolateConfigSource;
import com.scottlogic.deg.orchestrator.violate.ViolationFiltersProvider;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class ViolationFiltersProviderTest {

    @Test
    void whenNullConstraintsToViolate_ReturnEmptyListOfViolationFilters() {
        ViolateConfigSource configSource = mock(ViolateConfigSource.class);
        when(configSource.getConstraintsToNotViolate()).thenReturn(null);
        ViolationFiltersProvider provider = new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        assertThat(provider.get(), is(empty()));
    }

    @Test
    void whenEmptyConstraintsToViolate_ReturnEmptyListOfViolationFilters() {
        ViolateConfigSource configSource = mock(ViolateConfigSource.class);
        when(configSource.getConstraintsToNotViolate()).thenReturn(Collections.emptyList());
        ViolationFiltersProvider provider = new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        assertThat(provider.get(), is(empty()));
    }

    @Test
    void hasLengthConstraintsToViolate_ReturnsOneFilter_ThatDoesNotAcceptHasLengthConstraints() {
        ViolateConfigSource configSource = mock(ViolateConfigSource.class);
        when(configSource.getConstraintsToNotViolate())
            .thenReturn(Arrays.asList(AtomicConstraintType.HAS_LENGTH));
        ViolationFiltersProvider provider =
            new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        List<ViolationFilter> filters = provider.get();
        assertThat(filters, hasSize(1));
        assertThat(filters.get(0), instanceOf(ConstraintTypeViolationFilter.class));
        ConstraintTypeViolationFilter filter = (ConstraintTypeViolationFilter) filters.get(0);


        assertThat(filter.canViolate(
            new StringHasLengthConstraint(null, 2, Collections.emptySet())),
            is(false));

        assertThat(filter.canViolate(
            new IsStringShorterThanConstraint(null, 5, Collections.emptySet())),
            is(true));
    }

    @Test
    void twoConstraintsToViolate_ReturnListWithTwoFilter() {
        ViolateConfigSource configSource = mock(ViolateConfigSource.class);
        when(configSource.getConstraintsToNotViolate())
            .thenReturn(Arrays.asList(AtomicConstraintType.HAS_LENGTH, AtomicConstraintType.IS_IN_SET));
        ViolationFiltersProvider provider =
            new ViolationFiltersProvider(configSource, new AtomicConstraintTypeMapper());

        assertThat(provider.get(), hasSize(2));
    }
}