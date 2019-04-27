package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.constraints.atomic.StringHasLengthConstraint;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

class FieldSpecTests {
    @Test
    void equals_objIsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals(null);

        assertFalse(
            "Expected that when the other object is null a false value is returned but was true",
            result
        );
    }

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
            .withSetRestrictions(SetRestrictions.fromWhitelist(Collections.singleton("whitelist")), null);

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
            FieldSpec.Empty.withSetRestrictions(
                SetRestrictions.fromWhitelist(Collections.singleton("whitelist")),
                null)
        );

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withSetRestrictions(
                SetRestrictions.fromWhitelist(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3)
                    )),
                null);

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withSetRestrictions(
                SetRestrictions.fromWhitelist(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3, 4)
                    )),
                null)
        );

        assertFalse(
            "Expected that when the items in the set restrictions are not equal a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            null);

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
                new NumericRestrictions(),
                null)
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
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(firstFieldSpecRestrictions, null);

        NumericRestrictions secondFieldSpecRestrictions = new NumericRestrictions();
        secondFieldSpecRestrictions.min = new NumericLimit<>(new BigDecimal(5), false);
        secondFieldSpecRestrictions.max = new NumericLimit<>(new BigDecimal(20), false);
        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withNumericRestrictions(secondFieldSpecRestrictions, null)
        );

        assertFalse(
            "Expected that when the numeric restriction values differ a false value is returned but was true",
            result
        );
    }

    @Test
    public void shouldCreateNewInstanceWithSetRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        SetRestrictions restrictions = mock(SetRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withSetRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getSetRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNumericRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        NumericRestrictions restrictions = mock(NumericRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withNumericRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getNumericRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithStringRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        StringRestrictions restrictions = mock(StringRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withStringRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getStringRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithTypeRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        TypeRestrictions restrictions = mock(TypeRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withTypeRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getTypeRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithNullRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        NullRestrictions restrictions = mock(NullRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withNullRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getNullRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithDateTimeRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        DateTimeRestrictions restrictions = mock(DateTimeRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withDateTimeRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getDateTimeRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithFormatRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        FormatRestrictions restrictions = mock(FormatRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withFormatRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getFormatRestrictions(), restrictions);
    }

    @Test
    public void shouldCreateNewInstanceWithMustContainsRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        MustContainRestriction restrictions = mock(MustContainRestriction.class);
        FieldSpec augmentedFieldSpec = original.withMustContainRestriction(restrictions);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getMustContainRestriction(), restrictions);
    }

    @Test
    public void emptyFieldSpecsShouldBeEqualAndHaveSameHashCode(){
        FieldSpec first = FieldSpec.Empty;
        FieldSpec second = FieldSpec.Empty;

        Assert.assertThat(first, equalTo(second));
        Assert.assertThat(first.hashCode(), equalTo(second.hashCode()));
    }

    @Test
    public void emptyFieldSpecAugmentedWithNothingShouldBeEqualToEmpty(){
        FieldSpec first = FieldSpec.Empty;
        FieldSpec augmented = FieldSpec.Empty.withNullRestrictions(null, FieldSpecSource.Empty);

        Assert.assertThat(first, equalTo(augmented));
        Assert.assertThat(first.hashCode(), equalTo(augmented.hashCode()));
    }

    @Test
    public void fieldSpecsWithEqualSetRestrictionsShouldBeEqual(){
        SetRestrictions aRestrictions = new MockSetRestrictions(true);
        SetRestrictions bRestrictions = new MockSetRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withSetRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withSetRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalSetRestrictionsShouldBeUnequal(){
        SetRestrictions aRestrictions = new MockSetRestrictions(false);
        SetRestrictions bRestrictions = new MockSetRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withSetRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withSetRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNumericRestrictionsShouldBeEqual(){
        NumericRestrictions aRestrictions = new MockNumericRestrictions(true);
        NumericRestrictions bRestrictions = new MockNumericRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withNumericRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withNumericRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNumericRestrictionsShouldBeUnequal(){
        NumericRestrictions aRestrictions = new MockNumericRestrictions(false);
        NumericRestrictions bRestrictions = new MockNumericRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withNumericRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withNumericRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualStringRestrictionsShouldBeEqual(){
        StringRestrictions aRestrictions = new MockStringRestrictions(true);
        StringRestrictions bRestrictions = new MockStringRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withStringRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withStringRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalStringRestrictionsShouldBeUnequal(){
        StringRestrictions aRestrictions = new MockStringRestrictions(false);
        StringRestrictions bRestrictions = new MockStringRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withStringRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withStringRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualTypeRestrictionsShouldBeEqual(){
        TypeRestrictions aRestrictions = new MockTypeRestrictions(true);
        TypeRestrictions bRestrictions = new MockTypeRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withTypeRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withTypeRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalTypeRestrictionsShouldBeUnequal(){
        TypeRestrictions aRestrictions = new MockTypeRestrictions(false);
        TypeRestrictions bRestrictions = new MockTypeRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withTypeRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withTypeRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualNullRestrictionsShouldBeEqual(){
        NullRestrictions aRestrictions = new MockNullRestrictions(true);
        NullRestrictions bRestrictions = new MockNullRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withNullRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withNullRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalNullRestrictionsShouldBeUnequal(){
        NullRestrictions aRestrictions = new MockNullRestrictions(false);
        NullRestrictions bRestrictions = new MockNullRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withNullRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withNullRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualDateTimeRestrictionsShouldBeEqual(){
        DateTimeRestrictions aRestrictions = new MockDateTimeRestrictions(true);
        DateTimeRestrictions bRestrictions = new MockDateTimeRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withDateTimeRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withDateTimeRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalDateTimeRestrictionsShouldBeUnequal(){
        DateTimeRestrictions aRestrictions = new MockDateTimeRestrictions(false);
        DateTimeRestrictions bRestrictions = new MockDateTimeRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withDateTimeRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withDateTimeRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }
    
    @Test
    public void fieldSpecsWithEqualFormatRestrictionsShouldBeEqual(){
        FormatRestrictions aRestrictions = new MockFormatRestrictions(true);
        FormatRestrictions bRestrictions = new MockFormatRestrictions(true);
        FieldSpec a = FieldSpec.Empty.withFormatRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withFormatRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalFormatRestrictionsShouldBeUnequal(){
        FormatRestrictions aRestrictions = new MockFormatRestrictions(false);
        FormatRestrictions bRestrictions = new MockFormatRestrictions(false);
        FieldSpec a = FieldSpec.Empty.withFormatRestrictions(aRestrictions, FieldSpecSource.Empty);
        FieldSpec b = FieldSpec.Empty.withFormatRestrictions(bRestrictions, FieldSpecSource.Empty);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsWithEqualMustContainsRestrictionsShouldBeEqual(){
        MustContainRestriction aRestrictions = new MockMustContainRestriction(true);
        MustContainRestriction bRestrictions = new MockMustContainRestriction(true);
        FieldSpec a = FieldSpec.Empty.withMustContainRestriction(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withMustContainRestriction(bRestrictions);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsWithUnequalMustContainsRestrictionsShouldBeUnequal(){
        MustContainRestriction aRestrictions = new MockMustContainRestriction(false);
        MustContainRestriction bRestrictions = new MockMustContainRestriction(false);
        FieldSpec a = FieldSpec.Empty.withMustContainRestriction(aRestrictions);
        FieldSpec b = FieldSpec.Empty.withMustContainRestriction(bRestrictions);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void fieldSpecsShouldBeEntirelyEqual(){
        FieldSpec a = FieldSpec.Empty
            .withSetRestrictions(new MockSetRestrictions(true), FieldSpecSource.Empty)
            .withNumericRestrictions(new MockNumericRestrictions(true), FieldSpecSource.Empty)
            .withStringRestrictions(new MockStringRestrictions(true), FieldSpecSource.Empty)
            .withTypeRestrictions(new MockTypeRestrictions(true), FieldSpecSource.Empty)
            .withNullRestrictions(new MockNullRestrictions(true), FieldSpecSource.Empty)
            .withDateTimeRestrictions(new MockDateTimeRestrictions(true), FieldSpecSource.Empty)
            .withFormatRestrictions(new MockFormatRestrictions(true), FieldSpecSource.Empty)
            .withMustContainRestriction(new MockMustContainRestriction(true));
        FieldSpec b = FieldSpec.Empty
            .withSetRestrictions(new MockSetRestrictions(true), FieldSpecSource.Empty)
            .withNumericRestrictions(new MockNumericRestrictions(true), FieldSpecSource.Empty)
            .withStringRestrictions(new MockStringRestrictions(true), FieldSpecSource.Empty)
            .withTypeRestrictions(new MockTypeRestrictions(true), FieldSpecSource.Empty)
            .withNullRestrictions(new MockNullRestrictions(true), FieldSpecSource.Empty)
            .withDateTimeRestrictions(new MockDateTimeRestrictions(true), FieldSpecSource.Empty)
            .withFormatRestrictions(new MockFormatRestrictions(true), FieldSpecSource.Empty)
            .withMustContainRestriction(new MockMustContainRestriction(true));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void fieldSpecsShouldBeEntirelyUnequal(){
        FieldSpec a = FieldSpec.Empty
            .withSetRestrictions(new MockSetRestrictions(false), FieldSpecSource.Empty)
            .withNumericRestrictions(new MockNumericRestrictions(false), FieldSpecSource.Empty)
            .withStringRestrictions(new MockStringRestrictions(false), FieldSpecSource.Empty)
            .withTypeRestrictions(new MockTypeRestrictions(false), FieldSpecSource.Empty)
            .withNullRestrictions(new MockNullRestrictions(false), FieldSpecSource.Empty)
            .withDateTimeRestrictions(new MockDateTimeRestrictions(false), FieldSpecSource.Empty)
            .withFormatRestrictions(new MockFormatRestrictions(false), FieldSpecSource.Empty)
            .withMustContainRestriction(new MockMustContainRestriction(false));
        FieldSpec b = FieldSpec.Empty
            .withSetRestrictions(new MockSetRestrictions(false), FieldSpecSource.Empty)
            .withNumericRestrictions(new MockNumericRestrictions(false), FieldSpecSource.Empty)
            .withStringRestrictions(new MockStringRestrictions(false), FieldSpecSource.Empty)
            .withTypeRestrictions(new MockTypeRestrictions(false), FieldSpecSource.Empty)
            .withNullRestrictions(new MockNullRestrictions(false), FieldSpecSource.Empty)
            .withDateTimeRestrictions(new MockDateTimeRestrictions(false), FieldSpecSource.Empty)
            .withFormatRestrictions(new MockFormatRestrictions(false), FieldSpecSource.Empty)
            .withMustContainRestriction(new MockMustContainRestriction(false));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @ParameterizedTest()
    @MethodSource("partiallyUnequalProvider")
    public void fieldSpecsThatArePartiallyEqualShouldBeReportedAsUnequal(
        boolean setRestrictionsEqual,
        boolean numericRestrictionsEqual,
        boolean stringRestrictionsEqual,
        boolean typeRestrictionsEqual,
        boolean nullRestrictionsEqual,
        boolean dateTimeRestrictionsEqual,
        boolean formatRestrictionsEqual,
        boolean mustContainRestrictionsEqual){

        FieldSpec a = FieldSpec.Empty
            .withSetRestrictions(new MockSetRestrictions(setRestrictionsEqual), FieldSpecSource.Empty)
            .withNumericRestrictions(new MockNumericRestrictions(numericRestrictionsEqual), FieldSpecSource.Empty)
            .withStringRestrictions(new MockStringRestrictions(stringRestrictionsEqual), FieldSpecSource.Empty)
            .withTypeRestrictions(new MockTypeRestrictions(typeRestrictionsEqual), FieldSpecSource.Empty)
            .withNullRestrictions(new MockNullRestrictions(nullRestrictionsEqual), FieldSpecSource.Empty)
            .withDateTimeRestrictions(new MockDateTimeRestrictions(dateTimeRestrictionsEqual), FieldSpecSource.Empty)
            .withFormatRestrictions(new MockFormatRestrictions(formatRestrictionsEqual), FieldSpecSource.Empty)
            .withMustContainRestriction(new MockMustContainRestriction(mustContainRestrictionsEqual));
        FieldSpec b = FieldSpec.Empty
            .withSetRestrictions(new MockSetRestrictions(setRestrictionsEqual), FieldSpecSource.Empty)
            .withNumericRestrictions(new MockNumericRestrictions(numericRestrictionsEqual), FieldSpecSource.Empty)
            .withStringRestrictions(new MockStringRestrictions(stringRestrictionsEqual), FieldSpecSource.Empty)
            .withTypeRestrictions(new MockTypeRestrictions(typeRestrictionsEqual), FieldSpecSource.Empty)
            .withNullRestrictions(new MockNullRestrictions(nullRestrictionsEqual), FieldSpecSource.Empty)
            .withDateTimeRestrictions(new MockDateTimeRestrictions(dateTimeRestrictionsEqual), FieldSpecSource.Empty)
            .withFormatRestrictions(new MockFormatRestrictions(formatRestrictionsEqual), FieldSpecSource.Empty)
            .withMustContainRestriction(new MockMustContainRestriction(mustContainRestrictionsEqual));

        Assert.assertThat(a, not(equalTo(b)));
    }

    private static Stream<Arguments> partiallyUnequalProvider(){
        return Stream.of(
            Arguments.of(false, true, true, true, true, true, true, true),
            Arguments.of(true, false, true, true, true, true, true, true),
            Arguments.of(true, true, false, true, true, true, true, true),
            Arguments.of(true, true, true, false, true, true, true, true),
            Arguments.of(true, true, true, true, false, true, true, true),
            Arguments.of(true, true, true, true, true, false, true, true),
            Arguments.of(true, true, true, true, true, true, false, true),
            Arguments.of(true, true, true, true, true, true, true, false)
        );
    }

    private class MockSetRestrictions extends SetRestrictions{
        private final boolean isEqual;

        MockSetRestrictions(boolean isEqual) {
            super(Collections.emptySet(), Collections.emptySet());
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

    private class MockStringRestrictions extends StringRestrictions{
        private final boolean isEqual;

        MockStringRestrictions(boolean isEqual) {
            super(new StringConstraintsCollection(
                new StringHasLengthConstraint(
                    new Field("field"),
                    10,
                    Collections.emptySet())));
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

    private class MockNullRestrictions extends NullRestrictions{
        private final boolean isEqual;

        MockNullRestrictions(boolean isEqual) {
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
    
    private class MockFormatRestrictions extends FormatRestrictions{
        private final boolean isEqual;

        MockFormatRestrictions(boolean isEqual) {
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

    private class MockMustContainRestriction extends MustContainRestriction{
        private final boolean isEqual;

        MockMustContainRestriction(boolean isEqual) {
            super(Collections.emptySet());
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