package com.scottlogic.deg.common.profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecificFieldTypeTest {

    @Test
    void getDefaultFormatting_returnsNull_WhenPassedISINConstraint() {
        String result = SpecificFieldType.ISIN.getDefaultFormatting();
        assertNull(result);
    }

    @Test
    void getDefaultFormatting_returnsCorrectFormatting_WhenPassedDateConstraint() {
        String result = SpecificFieldType.DATE.getDefaultFormatting();
        assertEquals("%tF",result);
    }
}