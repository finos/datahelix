package com.scottlogic.deg.common.profile.fields;

import org.junit.jupiter.api.Test;

import static com.scottlogic.deg.common.util.Defaults.DEFAULT_DATE_FORMATTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SpecificFieldTypeTest {

    @Test
    void getDefaultFormatting_returnsNull_WhenPassedISINConstraint() {
        String result = SpecificFieldType.ISIN.getDefaultFormatting();
        assertNull(result);
    }

    @Test
    void getDefaultFormatting_returnsCorrectFormatting_WhenPassedDateConstraint() {
        String result = SpecificFieldType.DATE.getDefaultFormatting();
        assertEquals(DEFAULT_DATE_FORMATTING,result);
    }
}