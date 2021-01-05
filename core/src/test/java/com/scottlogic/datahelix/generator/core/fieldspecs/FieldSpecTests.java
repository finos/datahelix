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

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.datahelix.generator.core.restrictions.TypedRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.Limit;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Set;

import static com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsFactory.forMaxLength;
import static com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults.NUMERIC_MAX_LIMIT;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FieldSpecTests {
    @Test
    void equals_objTypeIsNotFieldSpec_returnsFalse() {
        FieldSpec fieldSpec = FieldSpecFactory.fromType(FieldType.STRING);

        boolean result = fieldSpec.equals("Test");

        assertFalse(
            "Expected that when the other object type is not FieldSpec a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecHasSetRestrictionsAndOtherObjectSetRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpecFactory.fromSingleLegalValue("legalValue");

        boolean result = fieldSpec.equals(FieldSpecFactory.fromType(FieldType.STRING));

        assertFalse(
            "Expected that when the field spec has set restrictions and the other object set restrictions are null a false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNullAndOtherObjectHasSetRestrictions_returnsFalse() {
        FieldSpec fieldSpec = FieldSpecFactory.fromType(FieldType.STRING);

        boolean result = fieldSpec.equals(FieldSpecFactory.fromSingleLegalValue("legalValue"));

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = FieldSpecFactory.fromLegalValuesList(Arrays.asList(1, 2, 3));

        boolean result = fieldSpec.equals(FieldSpecFactory.fromLegalValuesList(Arrays.asList(1, 2, 3, 4)));

        assertFalse(
            "Expected that when the items in the set restrictions are not equal a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNotNullAndNumericRestrictionsAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> firstFieldSpecRestrictions = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(1), true),
            new Limit<>(new BigDecimal(20), true));
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(firstFieldSpecRestrictions);

        LinearRestrictions<BigDecimal> secondFieldSpecRestrictions = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(5), true) ,
            new Limit<>(new BigDecimal(20), true));
        boolean result = fieldSpec.equals(
            FieldSpecFactory.fromRestriction(secondFieldSpecRestrictions)
        );

        assertFalse(
            "Expected that when the numeric restriction values differ a false value is returned but was true",
            result
        );
    }

    @Test
    public void shouldCreateNewInstanceWithNullRestrictions() {
        FieldSpec original = FieldSpecFactory.fromType(FieldType.STRING);
        FieldSpec augmentedFieldSpec = original.withNotNull();

        Assert.assertNotSame(original, augmentedFieldSpec);
    }

    @Test
    public void emptyFieldSpecsShouldBeEqualAndHaveSameHashCode() {
        FieldSpec first = FieldSpecFactory.fromType(FieldType.STRING);
        FieldSpec second = FieldSpecFactory.fromType(FieldType.STRING);

        Assert.assertThat(first, equalTo(second));
        Assert.assertThat(first.hashCode(), equalTo(second.hashCode()));
    }

    @Test
    public void fieldSpecsWithEqualSetRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpecFactory.fromSingleLegalValue("same");
        FieldSpec b = FieldSpecFactory.fromSingleLegalValue("same");

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalSetRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpecFactory.fromSingleLegalValue("not same");
        FieldSpec b = FieldSpecFactory.fromSingleLegalValue("different");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualRestrictionsShouldBeEqual() {
        TypedRestrictions aRestrictions = new MockedRestrictions(true);
        TypedRestrictions bRestrictions = new MockedRestrictions(true);
        FieldSpec a = FieldSpecFactory.fromRestriction(aRestrictions);
        FieldSpec b = FieldSpecFactory.fromRestriction(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalRestrictionsShouldBeUnequal() {
        TypedRestrictions aRestrictions = new MockedRestrictions(false);
        TypedRestrictions bRestrictions = new MockedRestrictions(false);
        FieldSpec a = FieldSpecFactory.fromRestriction(aRestrictions);
        FieldSpec b = FieldSpecFactory.fromRestriction(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualTypeRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpecFactory.fromType(FieldType.STRING);
        FieldSpec b = FieldSpecFactory.fromType(FieldType.STRING);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithEqualNullRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpecFactory.fromType(FieldType.STRING).withNotNull();
        FieldSpec b = FieldSpecFactory.fromType(FieldType.STRING).withNotNull();

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNullRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpecFactory.fromType(FieldType.STRING).withNotNull();
        FieldSpec b = FieldSpecFactory.nullOnly();

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    void permitsRejectsInvalidNumeric() {
        LinearRestrictions<BigDecimal> numeric = LinearRestrictionsFactory.createNumericRestrictions(new Limit<>(BigDecimal.TEN, true), NUMERIC_MAX_LIMIT);
        FieldSpec spec = FieldSpecFactory.fromRestriction(numeric);

        assertFalse(spec.canCombineWithLegalValue(BigDecimal.ONE));
    }

    @Test
    void permitsRejectsInvalidDateTime() {
        LinearRestrictions<OffsetDateTime> dateTime = LinearRestrictionsFactory.createDefaultDateTimeRestrictions();
        FieldSpec spec = FieldSpecFactory.fromRestriction(dateTime);

        OffsetDateTime time = OffsetDateTime.of(100, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        assertFalse(spec.canCombineWithLegalValue(time.plusNanos(1_000_000)));
    }

    @Test
    void fromType_whenDateTime_createDefaultDateTimeRestriction() {
        RestrictionsFieldSpec actual = FieldSpecFactory.fromType(FieldType.DATETIME);
        LinearRestrictions<OffsetDateTime> expected = LinearRestrictionsFactory.createDefaultDateTimeRestrictions();

        Assert.assertEquals(expected, actual.getRestrictions());
    }

    @Test
    void fromType_whenNumeric_createDefaultNumericRestriction() {
        RestrictionsFieldSpec actual = FieldSpecFactory.fromType(FieldType.NUMERIC);
        LinearRestrictions<BigDecimal> expected = LinearRestrictionsFactory.createDefaultNumericRestrictions();

        Assert.assertEquals(expected, actual.getRestrictions());
    }

    @Test
    void fromType_whenString_createDefaultStringRestriction() {
        RestrictionsFieldSpec actual = FieldSpecFactory.fromType(FieldType.STRING);
        StringRestrictions expected = forMaxLength(1000);

        Assert.assertEquals(expected, actual.getRestrictions());
    }

    @Test
    void permitsRejectsInvalidString() {
        TypedRestrictions mockTypedRestrictions = mock(TypedRestrictions.class);
        when(mockTypedRestrictions.match(any())).thenReturn(false);

        FieldSpec spec = FieldSpecFactory.fromRestriction(mockTypedRestrictions);

        assertFalse(spec.canCombineWithLegalValue("Anything"));
    }

    @Test
    void permits_whenCanCombineWithLegalValue_returnsFalse() {
        FieldSpec spec = FieldSpecFactory.fromSingleLegalValue(10);

        assertFalse(spec.canCombineWithLegalValue(11));
    }

    @Test
    void permits_whenCanCombineWithLegalValue_returnsTrue() {
        FieldSpec spec = FieldSpecFactory.fromSingleLegalValue(10);

        assertTrue(spec.canCombineWithLegalValue(10));
    }

    private class MockedRestrictions implements TypedRestrictions {
        private final boolean isEqual;

        MockedRestrictions(boolean isEqual) {
            this.isEqual = isEqual;
        }

        @Override
        public boolean equals(Object o) {
            return isEqual;
        }

        @Override
        public int hashCode() {
            return 1234;
        }

        @Override
        public boolean match(Object o) {
            return false;
        }

        @Override
        public FieldValueSource createFieldValueSource(Set blacklist) {
            return null;
        }
    }
}
