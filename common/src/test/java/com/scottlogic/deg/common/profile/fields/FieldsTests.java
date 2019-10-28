package com.scottlogic.deg.common.profile.fields;

import com.scottlogic.deg.common.ValidationException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.scottlogic.deg.common.profile.fields.FieldBuilder.createField;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FieldsTests
{
    @Test
    public void equals_objIsNull_returnsFalse()
    {
        Fields fields = Fields.create(Collections.singletonList(createField("Test")));
        boolean result = fields.equals(null);
        assertFalse("Expected when other object is null a false value is returned but was true", result);
    }

    @Test
    public void equals_objTypeIsNotProfileFields_returnsFalse()
    {
        Fields fields = Fields.create(Collections.singletonList(createField("Test")));
        boolean result = fields.equals("Test");
        assertFalse("Expected when the other object is a different type a false value is returned but was true", result);
    }

    @Test
    public void equals_rowSpecFieldsLengthNotEqualToOtherObjectFieldsLength_returnsFalse()
    {
        Fields fields = Fields.create(Arrays.asList(createField("First Field"), createField("Second Field")));
        boolean result = fields.equals(Fields.create(Collections.singletonList(createField("First Field"))));
        assertFalse("Expected when the fields length do not match a false value is returned but was true", result);
    }

    @Test
    public void equals_rowSpecFieldsLengthEqualToOtherObjectFieldsLengthButValuesDiffer_returnsFalse()
    {
        Fields fields = Fields.create(Arrays.asList(createField("First Field"), createField("Second Field")));
        boolean result = fields.equals(Fields.create((Arrays.asList(createField("First Field"),createField("Third Field")))));
        assertFalse("Expected when the values of the fields property differs from the fields of the other object a false value is returned but was true", result);
    }

    @Test
    public void equals_rowSpecFieldsAreEqualToTheFieldsOfTheOtherObject_returnsTrue()
    {
        Fields fields = Fields.create(Arrays.asList(createField("First Field"), createField("Second Field")));
        boolean result = fields.equals(Fields.create(Arrays.asList(createField("First Field"), createField("Second Field"))));
        assertTrue("Expected when the fields of both objects are equal a true value is returned but was false", result);
    }

    @Test
    public void hashCode_valuesInFieldsDifferInSize_returnsDifferentHashCodes()
    {
        Fields firstFields = Fields.create(Arrays.asList(createField("First Field"), createField("Second Field")));
        Fields secondFields = Fields.create(Arrays.asList(createField("First Field"),createField("Second Field"),createField("Third Field")));

        int firstHashCode = firstFields.hashCode();
        int secondHashCode = secondFields.hashCode();

        assertNotEquals("Expected that when the profile fields length differ the hash codes should not be the same but were equal",
            firstHashCode,secondHashCode);
    }

    @Test
    public void hashCode_valuesInFieldsAreEqualSizeButValuesDiffer_returnsDifferentHashCodes()
    {
        Fields firstFields = Fields.create(Arrays.asList(createField("First Field"), createField("Second Field")));
        Fields secondFields = Fields.create(Arrays.asList(createField("First Field"), createField("Third Field")));

        int firstHashCode = firstFields.hashCode();
        int secondHashCode = secondFields.hashCode();

        assertNotEquals("Expected when the fields length are equal but their values differ unique hash codes are returned but were equal",
            firstHashCode, secondHashCode);
    }

    @Test
    public  void hashCode_valuesInFieldsAreEqual_identicalHashCodesAreReturned()
    {
        Fields firstFields = Fields.create(Arrays.asList(createField("First Field"), createField("Second Field")));
        Fields secondFields = Fields.create(Arrays.asList(createField("First Field"),createField("Second Field")));

        int firstHashCode = firstFields.hashCode();
        int secondHashCode = secondFields.hashCode();

        assertEquals("Expected that when the profile fields are equal an equivalent hash code should be returned for both but were different",
            firstHashCode, secondHashCode);
    }

    @Test
    public  void create_fieldCollectionIsNull_validationExceptionIsThrown()
    {
        assertThrows(ValidationException.class, () -> Fields.create(null));
    }

    @Test
    public void create_fieldCollectionIsEmpty_validationExceptionIsThrown()
    {
        assertThrows(ValidationException.class, () -> Fields.create(new ArrayList<>()));
    }

    @Test
    public void create_namesInFieldCollectionAreNotDistinct_validationExceptionIsThrown()
    {
        Field firstField = createField("duplicate_field_name");
        Field secondField = createField("duplicate_field_name");
        assertThrows(ValidationException.class, () -> Fields.create(Arrays.asList(firstField, secondField)));
    }
}
