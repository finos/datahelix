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
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;
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
    private static TypedRestrictions leftNumeric;
    private static TypedRestrictions rightNumeric;
    private static TypedRestrictions leftString;
    private static TypedRestrictions rightString;
    private static TypedRestrictions leftDateTime;
    private static TypedRestrictions rightDateTime;

    @BeforeAll
    static void beforeAll() {
        leftNumeric = 
            createNumericRestrictions(
                new Limit<>(new BigDecimal("-1e10"), true),
                NUMERIC_MAX_LIMIT);
        rightNumeric = 
            createNumericRestrictions(
                new Limit<>(new BigDecimal("-1e15"), true),
                NUMERIC_MAX_LIMIT);

        leftString = (new StringRestrictionsFactory().forMaxLength(10));
        rightString = (new StringRestrictionsFactory().forMaxLength(12));

        leftDateTime = (createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT));
        rightDateTime = (createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT));
    }

    @BeforeEach
    void beforeEach(){
        linearMerger = mock(LinearRestrictionsMerger.class);
        StringMerger = mock(StringRestrictionsMerger.class);
        operation = new RestrictionsMergeOperation(linearMerger, StringMerger);
    }

    @Test
    void applyMergeOperation_withNumericRestrictions_shouldApplyRestriction(){
        Optional<TypedRestrictions> expected = Optional.of(rightNumeric);
        when(linearMerger.merge(leftNumeric, rightNumeric))
            .thenReturn(Optional.of(rightNumeric));

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftNumeric, rightNumeric);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withContradictoryNumericRestrictions_shouldPreventAnyValues(){
        Optional<TypedRestrictions> expected = Optional.empty();
        when(linearMerger.merge(leftNumeric, rightNumeric))
            .thenReturn(Optional.empty());

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftNumeric, rightNumeric);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withStringRestrictions_shouldApplyRestriction(){
        Optional<TypedRestrictions> expected = Optional.of(leftString);
        when(StringMerger.merge((StringRestrictions)leftString, (StringRestrictions)rightString))
            .thenReturn(Optional.of((StringRestrictions)leftString));

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftString, rightString);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(0)).merge(any(), any());
        verify(StringMerger, times(1)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withContradictoryStringRestrictions_shouldPreventAnyValues(){
        Optional<TypedRestrictions> expected = Optional.empty();
        when(StringMerger.merge((StringRestrictions)leftString, (StringRestrictions)rightString))
            .thenReturn(Optional.empty());

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftString, rightString);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(0)).merge(any(), any());
        verify(StringMerger, times(1)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withDateTimeRestrictions_shouldApplyRestriction(){
        Optional<TypedRestrictions> expected = Optional.of(rightDateTime);
            when(linearMerger.merge(leftDateTime, rightDateTime))
            .thenReturn(Optional.of(rightDateTime));

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftDateTime, rightDateTime);

        Assert.assertEquals(result, result);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }

    @Test
    void applyMergeOperation_withContradictoryDateTimeRestrictions_shouldPreventAnyValues(){
        Optional<TypedRestrictions> expected = Optional.empty();
        when(linearMerger.merge(leftDateTime, rightDateTime))
            .thenReturn(Optional.empty());

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftDateTime, rightDateTime);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(1)).merge(any(), any());
        verify(StringMerger, times(0)).merge(any(), any());
    }
}