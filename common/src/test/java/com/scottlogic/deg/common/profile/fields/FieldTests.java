package com.scottlogic.deg.common.profile.fields;

import com.scottlogic.deg.common.ValidationException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldTests
{
    @Test
    void create_fieldNameIsNull_throwsValidationException()
    {
        assertDoesNotThrow(() -> Field.create("test", SpecificFieldType.STRING, false,null,  false, true));
        assertThrows(ValidationException.class, () -> Field.create(null, SpecificFieldType.STRING, false,null,  false, true));
    }

    @Test
    void create_specificFieldTypeIsNull_throwsValidationException()
    {
        assertDoesNotThrow(() -> Field.create("test", SpecificFieldType.STRING, false,null,  false, true));
        assertThrows(ValidationException.class, () -> Field.create("test", null, false,null,  false, true));
    }

}