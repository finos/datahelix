package com.scottlogic.deg.common.profile;


import com.scottlogic.deg.common.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleTests
{
    @Test
    void create_constraintCollectionIsNull_validationExceptionIsThrown()
    {
        assertThrows(ValidationException.class, () -> Rule.create(null, null));
    }

    @Test
    void create_constraintCollectionIsEmpty_validationExceptionIsThrown()
    {
        assertThrows(ValidationException.class, () -> Rule.create(null, new ArrayList<>()));
    }
}