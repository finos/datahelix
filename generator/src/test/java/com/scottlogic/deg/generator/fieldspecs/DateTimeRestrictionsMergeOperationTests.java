/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.TypeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MIN_LIMIT;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateTimeRestrictionsMergeOperationTests {
    private LinearRestrictionsMerger merger;
    private DateTimeRestrictionsMergeOperation operation;
    private FieldSpec left;
    private FieldSpec right;

    @BeforeEach
    public void beforeEach() {
        merger = mock(LinearRestrictionsMerger.class);
        operation = new DateTimeRestrictionsMergeOperation(merger);

        left = FieldSpec.Empty.withDateTimeRestrictions(new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT));
        right = FieldSpec.Empty.withDateTimeRestrictions(new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT));
    }

    @Test
    public void applyMergeOperation_withNoDateTimeRestrictions_shouldNotApplyAnyRestrictions() {
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>(null));

        FieldSpec result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result, sameInstance(merging));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndNoTypeRestrictions_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty;
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        FieldSpec result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result, not(sameInstance(merging)));
        Assert.assertThat(result.getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.DATETIME)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictions_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.STRING, IsOfTypeConstraint.Types.DATETIME));
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        FieldSpec result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result, not(sameInstance(merging)));
        Assert.assertThat(result.getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.DATETIME)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndDateTimeTypeAlreadyNotPermitted_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.NUMERIC));
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        FieldSpec result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result, sameInstance(merging));
        Assert.assertThat(result.getDateTimeRestrictions(), is(nullValue()));
        Assert.assertThat(result.getTypeRestrictions(), not(nullValue()));
        Assert.assertThat(result.getTypeRestrictions().getAllowedTypes(), not(hasItem(IsOfTypeConstraint.Types.DATETIME)));
    }

    @Test
    public void applyMergeOperation_withContradictoryDateTimeRestrictionsAndDateTimeTypeOnlyPermittedType_shouldPreventAnyDateTimeValues() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.DATETIME));
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(MergeResult.unsuccessful());

        FieldSpec result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result, not(sameInstance(merging)));
        Assert.assertThat(result.getWhitelist().set(), is(empty()));
    }

    @Test @Disabled("ApplyMergeOperation will need to be refactored")
    public void applyMergeOperation_withMergableDateTimeRestrictions_shouldApplyMergedDateTimeRestrictions() {
        FieldSpec merging = FieldSpec.Empty
            .withTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.DATETIME));
        DateTimeRestrictions merged = new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT);
        when(merger.merge(left.getDateTimeRestrictions(), right.getDateTimeRestrictions()))
            .thenReturn(new MergeResult<>(merged));

        FieldSpec result = operation.applyMergeOperation(left, right, merging);

        Assert.assertThat(result, not(sameInstance(merging)));
        Assert.assertThat(result.getDateTimeRestrictions(), sameInstance(merged));
        Assert.assertThat(result.getTypeRestrictions(), sameInstance(merging.getTypeRestrictions()));
    }
}