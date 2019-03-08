package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.DataTypeRestrictions;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.NumericRestrictions;
import com.scottlogic.deg.generator.restrictions.NumericRestrictionsMerger;
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

class NumericRestrictionsMergeOperationTests {
    @Test
    public void applyMergeOperation_withNoNumericRestrictions_shouldNotApplyAnyRestriction(){
        NumericRestrictionsMerger merger = mock(NumericRestrictionsMerger.class);
        NumericRestrictionsMergeOperation operation = new NumericRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()))
            .thenReturn(new MergeResult<>(null));

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), sameInstance(merging));
    }

    @Test
    public void applyMergeOperation_withContradictoryNumericRestrictionsAndNoTypeRestrictions_shouldPreventAnyNumericValues(){
        NumericRestrictionsMerger merger = mock(NumericRestrictionsMerger.class);
        NumericRestrictionsMergeOperation operation = new NumericRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getNumericRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.NUMERIC)));
    }

    @Test
    public void applyMergeOperation_withContradictoryNumericRestrictions_shouldPreventAnyNumericValues(){
        NumericRestrictionsMerger merger = mock(NumericRestrictionsMerger.class);
        NumericRestrictionsMergeOperation operation = new NumericRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.STRING, IsOfTypeConstraint.Types.NUMERIC),
                FieldSpecSource.Empty);

        when(merger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getNumericRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.NUMERIC)));
    }

    @Test
    public void applyMergeOperation_withContradictoryNumericRestrictionsAndNumericTypeAlreadyNotPermitted_shouldPreventAnyNumericValues(){
        NumericRestrictionsMerger merger = mock(NumericRestrictionsMerger.class);
        NumericRestrictionsMergeOperation operation = new NumericRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.STRING), FieldSpecSource.Empty);

        when(merger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getNumericRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.NUMERIC)));
    }

    @Test
    public void applyMergeOperation_withContradictoryNumericRestrictionsAndNumericTypeOnlyPermittedType_shouldPreventAnyNumericValues(){
        NumericRestrictionsMerger merger = mock(NumericRestrictionsMerger.class);
        NumericRestrictionsMergeOperation operation = new NumericRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.NUMERIC), FieldSpecSource.Empty);

        when(merger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()))
            .thenReturn(new MergeResult<>());

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getNumericRestrictions(), is(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.get().getTypeRestrictions().getAllowedTypes(), empty());
    }

    @Test
    public void applyMergeOperation_withMergableNumericRestrictions_shouldApplyMergedNumericRestrictions(){
        NumericRestrictionsMerger merger = mock(NumericRestrictionsMerger.class);
        NumericRestrictionsMergeOperation operation = new NumericRestrictionsMergeOperation(merger);
        FieldSpec left = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec right = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty);
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.NUMERIC), FieldSpecSource.Empty);
        NumericRestrictions merged = new NumericRestrictions();
        when(merger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()))
            .thenReturn(new MergeResult<>(merged));

        Optional<FieldSpec> result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(sameInstance(merging)));
        Assert.assertThat(result.get().getNumericRestrictions(), sameInstance(merged));
        Assert.assertThat(result.get().getTypeRestrictions(), sameInstance(merging.getTypeRestrictions()));
    }
}