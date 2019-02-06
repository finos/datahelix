package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class FieldSpecTests {
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
    public void shouldCreateNewInstanceWithGranularityRestrictions(){
        FieldSpec original = FieldSpec.Empty;
        GranularityRestrictions restrictions = mock(GranularityRestrictions.class);
        FieldSpec augmentedFieldSpec = original.withGranularityRestrictions(restrictions, FieldSpecSource.Empty);

        Assert.assertNotSame(original, augmentedFieldSpec);
        Assert.assertSame(augmentedFieldSpec.getGranularityRestrictions(), restrictions);
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
}