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
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

class FieldSpecTests {

    @Test
    void equals_objTypeIsNotFieldSpec_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals("Test");

        assertFalse(
            "Expected that when the other object type is not FieldSpec a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecHasSetRestrictionsAndOtherObjectSetRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withWhitelist((FrequencyDistributedSet.uniform(Collections.singleton("whitelist"))));

        boolean result = fieldSpec.equals(FieldSpec.Empty);

        assertFalse(
            "Expected that when the field spec has set restrictions and the other object set restrictions are null a false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNullAndOtherObjectHasSetRestrictions_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.uniform(Collections.singleton("whitelist")))
        );

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withWhitelist(
                (FrequencyDistributedSet.uniform(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3)
                    ))));

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withWhitelist(
                (FrequencyDistributedSet.uniform(
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
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions());

        boolean result = fieldSpec.equals(FieldSpec.Empty);

        assertFalse(
            "Expected that when the field spec numeric restrictions is not null and the other object numeric restrictions are null a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNullAndOtherObjectNumericRestrictionsNotNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withNumericRestrictions(
                new NumericRestrictions())
        );

        assertFalse(
            "Expected that when the field spec does not have numeric restrictions and the other object has numeric restricitons are false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNotNullAndNumericRestrictionsAreNotEqual_returnsFalse() {
        NumericRestrictions firstFieldSpecRestrictions = new NumericRestrictions();
        firstFieldSpecRestrictions.min = new NumericLimit<>(new BigDecimal(1), false);
        firstFieldSpecRestrictions.max = new NumericLimit<>(new BigDecimal(20), false);
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(firstFieldSpecRestrictions);

        NumericRestrictions secondFieldSpecRestrictions = new NumericRestrictions();
        secondFieldSpecRestrictions.min = new NumericLimit<>(new BigDecimal(5), false);
        secondFieldSpecRestrictions.max = new NumericLimit<>(new BigDecimal(20), false);
        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withNumericRestrictions(secondFieldSpecRestrictions)
        );

        assertFalse(
            "Expected that when the numeric restriction values differ a false value is returned but was true",
            result
        );
    }

    @Test
    public void shouldCreateNewInstanceWithSetRestrictions() {
        FieldSpec original = FieldSpec.Empty;
        DistributedSet<Object> restrictions = FrequencyDistributedSet.uniform(Collections.singleton("whitelist"));
        FieldSpec augmentedFieldSpec = original.withWhitelist(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getWhitelist(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNumericRestrictions() {
        FieldSpec original = FieldSpec.Empty;
        NumericRestrictions restrictions = mock(NumericRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withNumericRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getNumericRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithStringRestrictions() {
        FieldSpec original = FieldSpec.Empty;
        StringRestrictions restrictions = mock(StringRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withStringRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getStringRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithTypeRestrictions() {
        FieldSpec original = FieldSpec.Empty;
        Collection<Types> restrictions = Collections.singleton(Types.STRING);
        FieldSpec augmentedFieldSpec = original.withTypeRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getTypeRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNullRestrictions() {
        FieldSpec original = FieldSpec.Empty;
        FieldSpec augmentedFieldSpec = original.withNotNull();

        Assert.assertNotSame(original, augmentedFieldSpec);
    }

    @Test
    public void shouldCreateNewInstanceWithDateTimeRestrictions() {
        FieldSpec original = FieldSpec.Empty;
        DateTimeRestrictions restrictions = mock(DateTimeRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withDateTimeRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getDateTimeRestrictions(), restrictions);
    }

    @Test
    public void emptyFieldSpecsShouldBeEqualAndHaveSameHashCode() {
        FieldSpec first = FieldSpec.Empty;
        FieldSpec second = FieldSpec.Empty;

        Assert.assertThat(first, equalTo(second));
        Assert.assertThat(first.hashCode(), equalTo(second.hashCode()));
    }

    @Test
    public void fieldSpecsWithEqualSetRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.uniform(Collections.singleton("same")));
        FieldSpec b = FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.uniform(Collections.singleton("same")));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalSetRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.uniform(Collections.singleton("not same")));
        FieldSpec b = FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.uniform(Collections.singleton("different")));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNumericRestrictionsShouldBeEqual() {
        NumericRestrictions aRestrictions = new MockNumericRestrictions(true);
        NumericRestrictions bRestrictions = new MockNumericRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withNumericRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withNumericRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNumericRestrictionsShouldBeUnequal() {
        NumericRestrictions aRestrictions = new MockNumericRestrictions(false);
        NumericRestrictions bRestrictions = new MockNumericRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withNumericRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withNumericRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualStringRestrictionsShouldBeEqual() {
        StringRestrictions aRestrictions = new MockStringRestrictions(true);
        StringRestrictions bRestrictions = new MockStringRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withStringRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withStringRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalStringRestrictionsShouldBeUnequal() {
        StringRestrictions aRestrictions = new MockStringRestrictions(false);
        StringRestrictions bRestrictions = new MockStringRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withStringRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withStringRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualTypeRestrictionsShouldBeEqual() {
        Collection<Types> aRestrictions = Collections.singleton(Types.STRING);
        Collection<Types> bRestrictions = Collections.singleton(Types.STRING);
        FieldSpec a = FieldSpec.Empty.withTypeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withTypeRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalTypeRestrictionsShouldBeUnequal() {
        Collection<Types> aRestrictions = Collections.singleton(Types.STRING);
        Collection<Types> bRestrictions = Collections.singleton(Types.DATETIME);
        FieldSpec a = FieldSpec.Empty.withTypeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withTypeRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNullRestrictionsShouldBeEqual() {
        FieldSpec a = FieldSpec.Empty.withNotNull();
        FieldSpec b = FieldSpec.Empty.withNotNull();

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNullRestrictionsShouldBeUnequal() {
        FieldSpec a = FieldSpec.Empty.withNotNull();
        FieldSpec b = FieldSpec.NullOnly;

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualDateTimeRestrictionsShouldBeEqual() {
        DateTimeRestrictions aRestrictions = new MockDateTimeRestrictions(true);
        DateTimeRestrictions bRestrictions = new MockDateTimeRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withDateTimeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withDateTimeRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalDateTimeRestrictionsShouldBeUnequal() {
        DateTimeRestrictions aRestrictions = new MockDateTimeRestrictions(false);
        DateTimeRestrictions bRestrictions = new MockDateTimeRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withDateTimeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withDateTimeRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }


    @Test
    void permitsRejectsInvalidNumeric() {
        NumericRestrictions numeric = new NumericRestrictions();
        FieldSpec spec = FieldSpec.Empty.withNumericRestrictions(numeric);

        numeric.min = new NumericLimit<>(BigDecimal.TEN, true);

        assertFalse(spec.permits(BigDecimal.ONE));
    }

    @Test
    void permitsRejectsInvalidDateTime() {
        DateTimeRestrictions dateTime = new DateTimeRestrictions();
        FieldSpec spec = FieldSpec.Empty.withDateTimeRestrictions(dateTime);

        OffsetDateTime time = OffsetDateTime.of(100, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        dateTime.max = new DateTimeLimit(time, true);

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
        FieldSpec spec = FieldSpec.Empty.withStringRestrictions(string);

        assertFalse(spec.permits("Anything"));
    }

    private class MockNumericRestrictions extends NumericRestrictions {
        private final boolean isEqual;

        MockNumericRestrictions(boolean isEqual) {
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
            return IsOfTypeConstraint.Types.DATETIME.isInstanceOf(o);
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

    private class MockDateTimeRestrictions extends DateTimeRestrictions {
        private final boolean isEqual;

        MockDateTimeRestrictions(boolean isEqual) {
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
    }

}
