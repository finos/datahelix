package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import static org.mockito.Mockito.mock;

class FixedFieldTests {
    @Test
    void getFieldSpecForCurrentValue_fieldSpecNullAndCurrentValueNull_returnsFieldSpecWithSetRestrictionsAndNotNullRestrictions() {
        FixedField fixedField = new FixedField(
            new Field("Test"),
            Stream.of(new ArrayList<Object>() {{ add(null); }}),
            FieldSpec.Empty,
            mock(ReductiveDataGeneratorMonitor.class)
        );

        // Stream must be collected to set the current value
        fixedField.getStream().collect(Collectors.toList());
        FieldSpec fieldSpec = fixedField.getFieldSpecForCurrentValue();

        Assert.assertNotNull(fieldSpec.getSetRestrictions());
        Assert.assertNotNull(fieldSpec.getNullRestrictions());
        Assert.assertEquals(Nullness.MUST_NOT_BE_NULL, fieldSpec.getNullRestrictions().nullness);
    }
}
