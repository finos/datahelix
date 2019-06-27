package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;
import com.scottlogic.deg.generator.generation.string.StringGenerator;
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
            .withWhitelist((
                Collections.singleton("whitelist")));

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
            FieldSpec.Empty.withWhitelist(
                (Collections.singleton("whitelist")))
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
                (
                    new HashSet<>(
                        Arrays.asList(1, 2, 3)
                    )));

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withWhitelist(
                (
                    new HashSet<>(
                        Arrays.asList(1, 2, 3, 4)
                    )))
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
    public void shouldCreateNewInstanceWithSetRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        Set<Object> restrictions = Collections.singleton("whitelist");
        FieldSpec augmentedFieldSpec = original.withWhitelist(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getWhitelist(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNumericRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        NumericRestrictions restrictions = mock(NumericRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withNumericRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getNumericRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithStringRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        StringRestrictions restrictions = mock(StringRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withStringRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getStringRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithTypeRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        TypeRestrictions restrictions = mock(TypeRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withTypeRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getTypeRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNullRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        FieldSpec augmentedFieldSpec = original.withNotNull();

        Assert.assertNotSame(original, augmentedFieldSpec);
    }

    @Test
    public void shouldCreateNewInstanceWithDateTimeRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        DateTimeRestrictions restrictions = mock(DateTimeRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withDateTimeRestrictions(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getDateTimeRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithFormatRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        FieldSpec augmentedFieldSpec = original.withFormatting("format");

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getFormatting(), "format");
    }

    @Test
    public void emptyFieldSpecsShouldBeEqualAndHaveSameHashCode(){
        FieldSpec first = FieldSpec.Empty;
        FieldSpec second = FieldSpec.Empty;

        Assert.assertThat(first, equalTo(second));
        Assert.assertThat(first.hashCode(), equalTo(second.hashCode()));
    }

    @Test
    public void fieldSpecsWithEqualSetRestrictionsShouldBeEqual(){
        FieldSpec a = FieldSpec.Empty.withWhitelist(Collections.singleton("same"));
        FieldSpec b = FieldSpec.Empty.withWhitelist(Collections.singleton("same"));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalSetRestrictionsShouldBeUnequal(){
        FieldSpec a = FieldSpec.Empty.withWhitelist(Collections.singleton("not same"));
        FieldSpec b = FieldSpec.Empty.withWhitelist(Collections.singleton("different"));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNumericRestrictionsShouldBeEqual(){
        NumericRestrictions aRestrictions = new MockNumericRestrictions(true);
        NumericRestrictions bRestrictions = new MockNumericRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withNumericRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withNumericRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNumericRestrictionsShouldBeUnequal(){
        NumericRestrictions aRestrictions = new MockNumericRestrictions(false);
        NumericRestrictions bRestrictions = new MockNumericRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withNumericRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withNumericRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualStringRestrictionsShouldBeEqual(){
        StringRestrictions aRestrictions = new MockStringRestrictions(true);
        StringRestrictions bRestrictions = new MockStringRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withStringRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withStringRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalStringRestrictionsShouldBeUnequal(){
        StringRestrictions aRestrictions = new MockStringRestrictions(false);
        StringRestrictions bRestrictions = new MockStringRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withStringRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withStringRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualTypeRestrictionsShouldBeEqual(){
        TypeRestrictions aRestrictions = new MockTypeRestrictions(true);
        TypeRestrictions bRestrictions = new MockTypeRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withTypeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withTypeRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalTypeRestrictionsShouldBeUnequal(){
        TypeRestrictions aRestrictions = new MockTypeRestrictions(false);
        TypeRestrictions bRestrictions = new MockTypeRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withTypeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withTypeRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNullRestrictionsShouldBeEqual(){
        FieldSpec a = FieldSpec.Empty.withNotNull();
        FieldSpec b = FieldSpec.Empty.withNotNull();

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNullRestrictionsShouldBeUnequal(){
        FieldSpec a = FieldSpec.Empty.withNotNull();
        FieldSpec b = FieldSpec.mustBeNull();

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualDateTimeRestrictionsShouldBeEqual(){
        DateTimeRestrictions aRestrictions = new MockDateTimeRestrictions(true);
        DateTimeRestrictions bRestrictions = new MockDateTimeRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withDateTimeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withDateTimeRestrictions(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalDateTimeRestrictionsShouldBeUnequal(){
        DateTimeRestrictions aRestrictions = new MockDateTimeRestrictions(false);
        DateTimeRestrictions bRestrictions = new MockDateTimeRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withDateTimeRestrictions(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withDateTimeRestrictions(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }
    
    @Test
    public void fieldSpecsWithEqualFormatRestrictionsShouldBeEqual(){
        FieldSpec a = FieldSpec.Empty.withFormatting("format");
        FieldSpec b = FieldSpec.Empty.withFormatting("format");

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalFormatRestrictionsShouldBeUnequal(){
        FieldSpec a = FieldSpec.Empty.withFormatting("format1");
        FieldSpec b = FieldSpec.Empty.withFormatting("format2");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsShouldBeEntirelyEqual(){
        FieldSpec a = FieldSpec.Empty
            .withNumericRestrictions(new MockNumericRestrictions(true))
            .withStringRestrictions(new MockStringRestrictions(true))
            .withTypeRestrictions(new MockTypeRestrictions(true))
            .withDateTimeRestrictions(new MockDateTimeRestrictions(true))
            .withFormatting("format");
        FieldSpec b = FieldSpec.Empty
            .withNumericRestrictions(new MockNumericRestrictions(true))
            .withStringRestrictions(new MockStringRestrictions(true))
            .withTypeRestrictions(new MockTypeRestrictions(true))
            .withDateTimeRestrictions(new MockDateTimeRestrictions(true))
            .withFormatting("format");

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsShouldBeEntirelyUnequal(){
        FieldSpec a = FieldSpec.Empty
            .withNumericRestrictions(new MockNumericRestrictions(false))
            .withStringRestrictions(new MockStringRestrictions(false))
            .withTypeRestrictions(new MockTypeRestrictions(false))
            .withDateTimeRestrictions(new MockDateTimeRestrictions(false))
            .withFormatting("format1");
        FieldSpec b = FieldSpec.Empty
            .withNumericRestrictions(new MockNumericRestrictions(false))
            .withStringRestrictions(new MockStringRestrictions(false))
            .withTypeRestrictions(new MockTypeRestrictions(false))
            .withDateTimeRestrictions(new MockDateTimeRestrictions(false))
            .withFormatting("format2");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @ParameterizedTest()
    @MethodSource("permitsNotNullProvider")
    public void permitsShouldRejectInvalidConfigurationsIfTypeRestrictionsIsNotNull(
        TypeCheckHolder holder) {
        FieldSpec spec = FieldSpec.Empty.withTypeRestrictions(holder.restrictions);
        assertEquals("Given " + holder.object + ", and types allowed " + holder.restrictions.getAllowedTypes() + ", expected " + holder.expectedSuccess , holder.expectedSuccess, spec.permits(holder.object));
    }

    private static class TypeCheckHolder {
        private final TypeRestrictions restrictions;
        private final Object object;
        private final boolean expectedSuccess;

        public TypeCheckHolder(TypeRestrictions restrictions, Object object, boolean expectedSuccess) {
            this.restrictions = restrictions;
            this.object = object;
            this.expectedSuccess = expectedSuccess;
        }
    }

    private static Stream<Arguments> permitsNotNullProvider() {
        TypeRestrictions numericRestrictions = new AnyTypeRestriction().except(Types.NUMERIC);
        TypeRestrictions stringRestrictions = new AnyTypeRestriction().except(Types.STRING);
        TypeRestrictions dateTimeRestrictions = new AnyTypeRestriction().except(Types.DATETIME);

        Integer intValue = 1;
        String stringValue = "a string";
        OffsetDateTime dateValue = OffsetDateTime.of(2001, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        Set<TypeRestrictions> typeRestrictions = SetUtils.setOf(numericRestrictions, stringRestrictions, dateTimeRestrictions);
        Set<Object> objects = SetUtils.setOf(intValue, stringValue, dateValue);

        Set<TypeCheckHolder> holders = new HashSet<>();
        for (TypeRestrictions restriction : typeRestrictions) {
            for (Object object : objects) {
                boolean success = !((restriction == numericRestrictions && object == intValue) ||
                    (restriction == stringRestrictions && object == stringValue) ||
                    (restriction == dateTimeRestrictions && object == dateValue));
                holders.add(new TypeCheckHolder(restriction, object, success));
            }
        }
        return holders.stream().map(Arguments::of);
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
        dateTime.max = new DateTimeRestrictions.DateTimeLimit(time, true);

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

    @ParameterizedTest()
    @MethodSource("partiallyUnequalProvider")
    public void fieldSpecsThatArePartiallyEqualShouldBeReportedAsUnequal(
        boolean numericRestrictionsEqual,
        boolean stringRestrictionsEqual,
        boolean typeRestrictionsEqual,
        boolean dateTimeRestrictionsEqual){

        FieldSpec a = FieldSpec.Empty
            .withNumericRestrictions(new MockNumericRestrictions(numericRestrictionsEqual))
            .withStringRestrictions(new MockStringRestrictions(stringRestrictionsEqual))
            .withTypeRestrictions(new MockTypeRestrictions(typeRestrictionsEqual))
            .withDateTimeRestrictions(new MockDateTimeRestrictions(dateTimeRestrictionsEqual));
        FieldSpec b = FieldSpec.Empty
            .withNumericRestrictions(new MockNumericRestrictions(numericRestrictionsEqual))
            .withStringRestrictions(new MockStringRestrictions(stringRestrictionsEqual))
            .withTypeRestrictions(new MockTypeRestrictions(typeRestrictionsEqual))
            .withDateTimeRestrictions(new MockDateTimeRestrictions(dateTimeRestrictionsEqual));

        Assert.assertThat(a, not(equalTo(b)));
    }

    private static Stream<Arguments> partiallyUnequalProvider(){
        return Stream.of(
            Arguments.of(false, true, true, true),
            Arguments.of(true, false, true, true),
            Arguments.of(true, true, false, true),
            Arguments.of(true, true, true, false)
        );
    }


    private class MockNumericRestrictions extends NumericRestrictions{
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

    private class MockStringRestrictions implements StringRestrictions{
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

    private class MockTypeRestrictions implements TypeRestrictions{
        private final boolean isEqual;

        MockTypeRestrictions(boolean isEqual) {
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
        public TypeRestrictions except(IsOfTypeConstraint.Types... types) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public boolean isTypeAllowed(IsOfTypeConstraint.Types type) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public TypeRestrictions intersect(TypeRestrictions other) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
            throw new UnsupportedOperationException("Not supported");
        }
    }


    private class MockDateTimeRestrictions extends DateTimeRestrictions{
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
