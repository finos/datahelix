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

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

import static com.scottlogic.deg.common.profile.FieldType.*;
import static com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory.forMaxLength;
import static com.scottlogic.deg.generator.utils.Defaults.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        FieldSpec fieldSpec = FieldSpecFactory.fromList(DistributedList.singleton("whitelist"));

        boolean result = fieldSpec.equals(FieldSpecFactory.fromType(FieldType.STRING));

        assertFalse(
            "Expected that when the field spec has set restrictions and the other object set restrictions are null a false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNullAndOtherObjectHasSetRestrictions_returnsFalse() {
        FieldSpec fieldSpec = FieldSpecFactory.fromType(FieldType.STRING);

        boolean result = fieldSpec.equals(
            FieldSpecFactory.fromList(DistributedList.singleton("whitelist")));

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = FieldSpecFactory.fromList(DistributedList.uniform(Arrays.asList(1, 2, 3)));

        boolean result = fieldSpec.equals(
            FieldSpecFactory.fromList(DistributedList.uniform(Arrays.asList(1, 2, 3, 4))));

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
        FieldSpec a = FieldSpecFactory.fromList(DistributedList.singleton("same"));
        FieldSpec b = FieldSpecFactory.fromList(DistributedList.singleton("same"));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalSetRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpecFactory.fromList(DistributedList.singleton("not same"));
        FieldSpec b = FieldSpecFactory.fromList(DistributedList.singleton("different"));

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

        assertFalse(spec.permits(BigDecimal.ONE));
    }

    @Test
    void permitsRejectsInvalidDateTime() {
        LinearRestrictions<OffsetDateTime> dateTime = LinearRestrictionsFactory.createDefaultDateTimeRestrictions();
        FieldSpec spec = FieldSpecFactory.fromRestriction(dateTime);

        OffsetDateTime time = OffsetDateTime.of(100, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        assertFalse(spec.permits(time.plusNanos(1_000_000)));
    }

    @Test
    void fromType_whenDateTime_createDefaultDateTimeRestriction() {
        FieldSpec actual = FieldSpecFactory.fromType(DATETIME);
        LinearRestrictions<OffsetDateTime> expected = LinearRestrictionsFactory.createDefaultDateTimeRestrictions();

        Assert.assertEquals(expected, actual.getRestrictions());
    }

    @Test
    void fromType_whenNumeric_createDefaultNumericRestriction() {
        FieldSpec actual = FieldSpecFactory.fromType(NUMERIC);
        LinearRestrictions<BigDecimal> expected = LinearRestrictionsFactory.createDefaultNumericRestrictions();

        Assert.assertEquals(expected, actual.getRestrictions());
    }

    @Test
    void fromType_whenString_createDefaultStringRestriction() {
        FieldSpec actual = FieldSpecFactory.fromType(STRING);
        StringRestrictions expected = forMaxLength(1000);

        Assert.assertEquals(expected, actual.getRestrictions());
    }

    @Test
    void permitsRejectsInvalidString() {
        StringRestrictions string = new StringRestrictions() {
            @Override
            public Optional<StringRestrictions> intersect(StringRestrictions other) {
                return null;
            }

            @Override
            public boolean match(String x) {
                return false;
            }

            @Override
            public StringGenerator createGenerator() {
                return null;
            }
        };
        FieldSpec spec = FieldSpecFactory.fromRestriction(string);

        assertFalse(spec.permits("Anything"));
    }

    @Test
    void permits_whenNotInWhiteList_returnsFalse() {
        FieldSpec spec = FieldSpecFactory.fromList(DistributedList.singleton(10));

        assertFalse(spec.permits(11));
    }

    @Test
    void permits_whenInWhiteList_returnsTrue() {
        FieldSpec spec = FieldSpecFactory.fromList(DistributedList.singleton(10));

        assertTrue(spec.permits(10));
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
    }
}
