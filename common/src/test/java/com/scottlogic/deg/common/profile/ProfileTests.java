package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.scottlogic.deg.common.profile.fields.FieldBuilder.createValidFields;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfileTests
{
    @Test
    void create_fieldsAreNull_validationExceptionIsThrown()
    {
        assertThrows(ValidationException.class, () -> Profile.create("description", null, new ArrayList<>()));
    }

    @Test
    void create_ruleCollectionIsNull_validationExceptionIsThrown()
    {
        assertThrows(ValidationException.class, () -> Profile.create("description", createValidFields(),null));
    }

   /* @Test
    void create_constraintReferencesUnknownField_validationExceptionIsThrown()
    {
        Field unknownField = createField("unknownField", SpecificFieldType.STRING);
        Constraint constraint = new EqualToConstraint(unknownField, "value");
        Rule rule = Rule.create("description", Stream.of(constraint).collect(Collectors.toList()));
        assertThrows(ValidationException.class, () -> Profile.create(null, createValidFields(), Stream.of(rule).collect(Collectors.toList())));
    }*/
}