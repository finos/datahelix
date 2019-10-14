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

import com.scottlogic.deg.generator.restrictions.StringRestrictions;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDateTimeRestrictions;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createNumericRestrictions;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MIN_LIMIT;
import static org.mockito.Mockito.*;

class RestrictionsMergeOperationTest {
    private LinearRestrictionsMerger linearMerger;
    private StringRestrictionsMerger StringMerger;
    private RestrictionsMergeOperation operation;
    private static FieldSpec leftNumeric;
    private static FieldSpec rightNumeric;
    private static FieldSpec leftString;
    private static FieldSpec rightString;
    private static FieldSpec leftDateTime;
    private static FieldSpec rightDateTime;

    @BeforeAll
    static void beforeAll() {
        leftNumeric = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(
                new Limit<>(new BigDecimal("-1e10"), true),
                NUMERIC_MAX_LIMIT)
        );
        rightNumeric = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(
                new Limit<>(new BigDecimal("-1e15"), true),
                NUMERIC_MAX_LIMIT)
        );

        leftString = FieldSpecFactory.fromRestriction(new StringRestrictionsFactory().forMaxLength(10));
        rightString = FieldSpecFactory.fromRestriction(new StringRestrictionsFactory().forMaxLength(12));

        leftDateTime = FieldSpecFactory.fromRestriction(createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT));
        rightDateTime = FieldSpecFactory.fromRestriction(createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT));
    }

    @BeforeEach
    void beforeEach(){
        linearMerger = mock(LinearRestrictionsMerger.class);
        StringMerger = mock(StringRestrictionsMerger.class);
        operation = new RestrictionsMergeOperation(linearMerger, StringMerger);
    }

    @Test
    void applyMergeOperation_withNumericRestrictions_shouldApplyRestriction(){
        when(linearMerger.merge(leftNumeric.getRestrictions(), rightNumeric.getRestrictions()))
            .thenReturn(Optional.of(rightNumeric.getRestrictions()));

        FieldSpec result = operation.applyMergeOperation(leftNumeric, rightNumeric);

        Assert.assertEquals(result, rightNumeric);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withContradictoryNumericRestrictions_shouldPreventAnyValues(){
        FieldSpec merging = FieldSpecFactory.nullOnly();
        when(linearMerger.merge(leftNumeric.getRestrictions(), rightNumeric.getRestrictions()))
            .thenReturn(Optional.empty());

        FieldSpec result = operation.applyMergeOperation(leftNumeric, rightNumeric);

        Assert.assertEquals(result, merging);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withStringRestrictions_shouldApplyRestriction(){
        when(StringMerger.merge((StringRestrictions)leftString.getRestrictions(), (StringRestrictions)rightString.getRestrictions()))
            .thenReturn(Optional.of((StringRestrictions)leftString.getRestrictions()));

        FieldSpec result = operation.applyMergeOperation(leftString, rightString);

        Assert.assertEquals(result, leftString);
        verify(linearMerger, times(0)).merge(any(), any());
        verify(StringMerger, times(1)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withContradictoryStringRestrictions_shouldPreventAnyValues(){
        FieldSpec merging = FieldSpecFactory.nullOnly();
        when(StringMerger.merge((StringRestrictions)leftString.getRestrictions(), (StringRestrictions)rightString.getRestrictions()))
            .thenReturn(Optional.empty());

        FieldSpec result = operation.applyMergeOperation(leftString, rightString);

        Assert.assertEquals(result, merging);
        verify(linearMerger, times(0)).merge(any(), any());
        verify(StringMerger, times(1)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withDateTimeRestrictions_shouldApplyRestriction(){
        when(linearMerger.merge(leftDateTime.getRestrictions(), rightDateTime.getRestrictions()))
            .thenReturn(Optional.of(rightDateTime.getRestrictions()));

        FieldSpec result = operation.applyMergeOperation(leftDateTime, rightDateTime);

        Assert.assertEquals(result, rightDateTime);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withContradictoryDateTimeRestrictions_shouldPreventAnyValues(){
        FieldSpec merging = FieldSpecFactory.nullOnly();
        when(linearMerger.merge(leftDateTime.getRestrictions(), rightDateTime.getRestrictions()))
            .thenReturn(Optional.empty());

        FieldSpec result = operation.applyMergeOperation(leftDateTime, rightDateTime);

        Assert.assertEquals(result, merging);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }
}