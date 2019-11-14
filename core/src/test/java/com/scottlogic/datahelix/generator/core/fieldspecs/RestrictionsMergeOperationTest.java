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

package com.scottlogic.datahelix.generator.core.fieldspecs;

import com.scottlogic.datahelix.generator.core.restrictions.bool.BooleanRestrictionsMerger;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsFactory;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsMerger;
import com.scottlogic.datahelix.generator.core.restrictions.TypedRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.Limit;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsMerger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory.createDateTimeRestrictions;
import static com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory.createNumericRestrictions;
import static com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults.NUMERIC_MAX_LIMIT;
import static com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults.DATETIME_MIN_LIMIT;
import static org.mockito.Mockito.*;

class RestrictionsMergeOperationTest {
    private LinearRestrictionsMerger linearMerger;
    private StringRestrictionsMerger stringMerger;
    private BooleanRestrictionsMerger booleanMerger;
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
        stringMerger = mock(StringRestrictionsMerger.class);
        booleanMerger = mock(BooleanRestrictionsMerger.class);
        operation = new RestrictionsMergeOperation(linearMerger, stringMerger, booleanMerger);
    }

    @Test
    void applyMergeOperation_withNumericRestrictions_shouldApplyRestriction(){
        Optional<TypedRestrictions> expected = Optional.of(rightNumeric);
        when(linearMerger.merge(leftNumeric, rightNumeric, false))
            .thenReturn(Optional.of(rightNumeric));

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftNumeric, rightNumeric, false);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(1)).merge(any(), any(), anyBoolean());
        verify(stringMerger, times(0)).merge(any(), any(), anyBoolean());
    }

    @Test
    void applyMergeOperation_withContradictoryNumericRestrictions_shouldPreventAnyValues(){
        Optional<TypedRestrictions> expected = Optional.empty();
        when(linearMerger.merge(leftNumeric, rightNumeric, false))
            .thenReturn(Optional.empty());

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftNumeric, rightNumeric, false);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(1)).merge(any(), any(), anyBoolean());
        verify(stringMerger, times(0)).merge(any(), any(), anyBoolean());
    }

    @Test
    void applyMergeOperation_withStringRestrictions_shouldApplyRestriction(){
        Optional<TypedRestrictions> expected = Optional.of(leftString);
        when(stringMerger.merge((StringRestrictions)leftString, (StringRestrictions)rightString, false))
            .thenReturn(Optional.of((StringRestrictions)leftString));

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftString, rightString, false);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(0)).merge(any(), any(), anyBoolean());
        verify(stringMerger, times(1)).merge(any(), any(), anyBoolean());
    }

    @Test
    void applyMergeOperation_withContradictoryStringRestrictions_shouldPreventAnyValues(){
        Optional<TypedRestrictions> expected = Optional.empty();
        when(stringMerger.merge((StringRestrictions)leftString, (StringRestrictions)rightString, false))
            .thenReturn(Optional.empty());

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftString, rightString, false);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(0)).merge(any(), any(), anyBoolean());
        verify(stringMerger, times(1)).merge(any(), any(), anyBoolean());
    }

    @Test
    void applyMergeOperation_withDateTimeRestrictions_shouldApplyRestriction(){
        Optional<TypedRestrictions> expected = Optional.of(rightDateTime);
            when(linearMerger.merge(leftDateTime, rightDateTime, false))
            .thenReturn(Optional.of(rightDateTime));

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftDateTime, rightDateTime, false);

        Assert.assertEquals(result, result);
        verify(linearMerger, times(1)).merge(any(), any(), anyBoolean());
        verify(stringMerger, times(0)).merge(any(), any(), anyBoolean());
    }

    @Test
    void applyMergeOperation_withContradictoryDateTimeRestrictions_shouldPreventAnyValues(){
        Optional<TypedRestrictions> expected = Optional.empty();
        when(linearMerger.merge(leftDateTime, rightDateTime, false))
            .thenReturn(Optional.empty());

        Optional<TypedRestrictions> result = operation.applyMergeOperation(leftDateTime, rightDateTime, false);

        Assert.assertEquals(expected, result);
        verify(linearMerger, times(1)).merge(any(), any(), anyBoolean());
        verify(stringMerger, times(0)).merge(any(), any(), anyBoolean());
    }
}