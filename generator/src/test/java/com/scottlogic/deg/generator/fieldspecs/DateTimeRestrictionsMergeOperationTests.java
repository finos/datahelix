package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.DataTypeRestrictions;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import org.junit.Assert;
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
    @Test
    public void applyMergeOperation_withNoDateTimeRestrictions_shouldNotApplyAnyRestrictions(){
        DateTimeRestrictionsMerger merger = mock(DateTimeRestrictionsMerger.class);
        DateTimeRestrictionsMergeOperation operation = new DateTimeRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>(null));

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), sameInstance(merging));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndNoTypeRestrictions_shouldPreventAnyTemporalValues(){
        DateTimeRestrictionsMerger merger = mock(DateTimeRestrictionsMerger.class);
        DateTimeRestrictionsMergeOperation operation = new DateTimeRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.TEMPORAL)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictions_shouldPreventAnyTemporalValues(){
        DateTimeRestrictionsMerger merger = mock(DateTimeRestrictionsMerger.class);
        DateTimeRestrictionsMergeOperation operation = new DateTimeRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.STRING, IsOfTypeConstraint.Types.TEMPORAL),
                FieldSpecSource.Empty);
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.TEMPORAL)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndTemporalTypeAlreadyNotPermitted_shouldPreventAnyTemporalValues(){
        DateTimeRestrictionsMerger merger = mock(DateTimeRestrictionsMerger.class);
        DateTimeRestrictionsMergeOperation operation = new DateTimeRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.NUMERIC), FieldSpecSource.Empty);
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.TEMPORAL)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndTemporalTypeOnlyPermittedType_shouldPreventAnyTemporalValues(){
        DateTimeRestrictionsMerger merger = mock(DateTimeRestrictionsMerger.class);
        DateTimeRestrictionsMergeOperation operation = new DateTimeRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.TEMPORAL), FieldSpecSource.Empty);
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), empty());
    }

    @Test
    public void applyMergeOperation_withMergableDateTimeRestrictions_shouldApplyMergedDateTimeRestrictions(){
        DateTimeRestrictionsMerger merger = mock(DateTimeRestrictionsMerger.class);
        DateTimeRestrictionsMergeOperation operation = new DateTimeRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.TEMPORAL), FieldSpecSource.Empty);
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