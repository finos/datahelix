package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.*;

class StandardRowSpecDataBagSourceFactoryTests {

    private static final Field field = new Field("Field 1");
    private static final ProfileFields fields = new ProfileFields(Collections.singletonList(field));

    @Test
    void shouldCreateValuesForEachFieldSpecInRowSpec() {
        FieldSpec fieldSpec = FieldSpec.Empty;
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);
        FieldSpecValueGenerator generatorFactory = mock(FieldSpecValueGenerator.class);
        RowSpecDataBagSourceFactory factory = new StandardRowSpecDataBagSourceFactory(generatorFactory, mock(GenerationConfig.class));

        factory.createDataBagSource(rowSpec);

        verify(generatorFactory, times(1)).generate(field, fieldSpec);
    }
}