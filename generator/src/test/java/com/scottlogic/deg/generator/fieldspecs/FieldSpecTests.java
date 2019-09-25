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

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.scottlogic.deg.common.profile.Types.*;
import static com.scottlogic.deg.generator.utils.Defaults.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

class FieldSpecTests {

    @Test
    void equals_objTypeIsNotFieldSpec_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.fromType(STRING);

        boolean result = fieldSpec.equals("Test");

        assertFalse(
            "Expected that when the other object type is not FieldSpec a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecHasSetRestrictionsAndOtherObjectSetRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.fromType(STRING)
            .withWhitelist((DistributedSet.uniform(Collections.singleton("whitelist"))));

        boolean result = fieldSpec.equals(FieldSpec.fromType(STRING));

        assertFalse(
            "Expected that when the field spec has set restrictions and the other object set restrictions are null a false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNullAndOtherObjectHasSetRestrictions_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.fromType(STRING);

        boolean result = fieldSpec.equals(
            FieldSpec.fromType(STRING).withWhitelist(DistributedSet.uniform(Collections.singleton("whitelist")))
        );

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC)
            .withWhitelist(
                (DistributedSet.uniform(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3)
                    ))));

        boolean result = fieldSpec.equals(
            FieldSpec.fromType(NUMERIC).withWhitelist(
                (DistributedSet.uniform(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3, 4)
                    ))))
        );

        assertFalse(
            "Expected that when the items in the set restrictions are not equal a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNotNullAndNumericRestrictionsAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> firstFieldSpecRestrictions = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(1), false),
            new Limit<>(new BigDecimal(20), false));
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(firstFieldSpecRestrictions);

        LinearRestrictions<BigDecimal> secondFieldSpecRestrictions = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(5), false) ,
            new Limit<>(new BigDecimal(20), false));
        boolean result = fieldSpec.equals(
            FieldSpec.fromType(NUMERIC).withNumericRestrictions(secondFieldSpecRestrictions)
        );

        assertFalse(
            "Expected that when the numeric restriction values differ a false value is returned but was true",
            result
        );
    }

    @Test
    public void shouldCreateNewInstanceWithSetRestrictions() {
        FieldSpec original = FieldSpec.fromType(STRING);
        DistributedSet<Object> restrictions = DistributedSet.uniform(Collections.singleton("whitelist"));
        FieldSpec augmentedFieldSpec = original.withWhitelist(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getWhitelist(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNumericRestrictions() {
        FieldSpec original = FieldSpec.fromType(NUMERIC);
        LinearRestrictions<BigDecimal> restrictions = mock(LinearRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withNumericRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getNumericRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithStringRestrictions() {
        FieldSpec original = FieldSpec.fromType(STRING);
        StringRestrictions restrictions = mock(StringRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withStringRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getStringRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNullRestrictions() {
        FieldSpec original = FieldSpec.fromType(NUMERIC);
        FieldSpec augmentedFieldSpec = original.withNotNull();

        Assert.assertNotSame(original, augmentedFieldSpec);
    }

    @Test
    public void shouldCreateNewInstanceWithDateTimeRestrictions() {
        FieldSpec original = FieldSpec.fromType(DATETIME);
        LinearRestrictions<OffsetDateTime> restrictions = mock(LinearRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withDateTimeRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getDateTimeRestrictions(), restrictions);
    }

    @Test
    public void emptyFieldSpecsShouldBeEqualAndHaveSameHashCode() {
        FieldSpec first = FieldSpec.fromType(NUMERIC);
        FieldSpec second = FieldSpec.fromType(NUMERIC);

        Assert.assertThat(first, equalTo(second));
        Assert.assertThat(first.hashCode(), equalTo(second.hashCode()));
    }

    @Test
    public void fieldSpecsWithEqualSetRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpec.fromType(STRING).withWhitelist(DistributedSet.uniform(Collections.singleton("same")));
        FieldSpec b = FieldSpec.fromType(STRING).withWhitelist(DistributedSet.uniform(Collections.singleton("same")));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalSetRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpec.fromType(STRING).withWhitelist(DistributedSet.uniform(Collections.singleton("not same")));
        FieldSpec b = FieldSpec.fromType(STRING).withWhitelist(DistributedSet.uniform(Collections.singleton("different")));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualStringRestrictionsShouldBeEqual() {
        StringRestrictions aRestrictions = new MockStringRestrictions(true);
        StringRestrictions bRestrictions = new MockStringRestrictions(true);
        FieldSpec a = FieldSpec.fromType(STRING).withStringRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.fromType(STRING).withStringRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalStringRestrictionsShouldBeUnequal() {
        StringRestrictions aRestrictions = new MockStringRestrictions(false);
        StringRestrictions bRestrictions = new MockStringRestrictions(false);
        FieldSpec a = FieldSpec.fromType(STRING).withStringRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.fromType(STRING).withStringRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualTypeRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpec.fromType(STRING);
        FieldSpec b = FieldSpec.fromType(STRING);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalTypeRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpec.fromType(STRING);
        FieldSpec b = FieldSpec.fromType(DATETIME);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNullRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpec.fromType(STRING).withNotNull();
        FieldSpec b = FieldSpec.fromType(STRING).withNotNull();

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNullRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpec.fromType(STRING).withNotNull();
        FieldSpec b = FieldSpec.nullOnlyFromType(STRING);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    void permitsRejectsInvalidNumeric() {
        LinearRestrictions<BigDecimal> numeric = LinearRestrictionsFactory.createNumericRestrictions(new Limit<>(BigDecimal.TEN, true), NUMERIC_MAX_LIMIT);
        FieldSpec spec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(numeric);

        assertFalse(spec.permits(BigDecimal.ONE));
    }

    @Test
    void permitsRejectsInvalidDateTime() {
        LinearRestrictions<OffsetDateTime> dateTime = LinearRestrictionsFactory.createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT);
        FieldSpec spec = FieldSpec.fromType(DATETIME).withDateTimeRestrictions(dateTime);

        OffsetDateTime time = OffsetDateTime.of(100, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        assertFalse(spec.permits(time.plusNanos(1_000_000)));
    }

    @Test
    void permitsRejectsInvalidString() {
        StringRestrictions string = new StringRestrictions() {
            @Override
            public MergeResult<StringRestrictions> intersect(StringRestrictions other) {
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
        FieldSpec spec = FieldSpec.fromType(STRING).withStringRestrictions(string);

        assertFalse(spec.permits("Anything"));
    }

    private class MockStringRestrictions implements StringRestrictions {
        private final boolean isEqual;

        MockStringRestrictions(boolean isEqual) {
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
        public MergeResult<StringRestrictions> intersect(StringRestrictions other) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public boolean match(String x) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public boolean isInstanceOf(Object o) {
            return DATETIME.isInstanceOf(o);
        }

        @Override
        public boolean match(Object x) {
            return false;
        }

        @Override
        public StringGenerator createGenerator() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
