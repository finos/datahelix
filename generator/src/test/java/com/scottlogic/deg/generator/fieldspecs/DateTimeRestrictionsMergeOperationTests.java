package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.DataTypeRestrictions;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateTimeRestrictionsMergeOperationTests {
    private DateTimeRestrictionsMerger merger;
    private DateTimeRestrictionsMergeOperation operation;
    private FieldSpec left;
    private FieldSpec right;

    @BeforeEach
    public void beforeEach() {
        merger = mock(DateTimeRestrictionsMerger.class);
        operation = new DateTimeRestrictionsMergeOperation(merger);

        left = FieldSpec.Empty.withDateTimeRestrictions(new DateTimeRestrictions());
        right = FieldSpec.Empty.withDateTimeRestrictions(new DateTimeRestrictions());
    }

    @Test
    public void applyMergeOperation_withNoDateTimeRestrictions_shouldNotApplyAnyRestrictions() {
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>(null));

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), sameInstance(merging));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndNoTypeRestrictions_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.DATETIME)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictions_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.STRING, IsOfTypeConstraint.Types.DATETIME));
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.DATETIME)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndDateTimeTypeAlreadyNotPermitted_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.NUMERIC));
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.DATETIME)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndDateTimeTypeOnlyPermittedType_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.DATETIME));
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), empty());
    }

    @Test
    public void applyMergeOperation_withMergableDateTimeRestrictions_shouldApplyMergedDateTimeRestrictions() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.DATETIME));
        DateTimeRestrictions merged = new DateTimeRestrictions();
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>(merged));

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), sameInstance(merged));
        Assert.assertThat(result.get().getTypeRestrictions(), sameInstance(merging.getTypeRestrictions()));
    }
}