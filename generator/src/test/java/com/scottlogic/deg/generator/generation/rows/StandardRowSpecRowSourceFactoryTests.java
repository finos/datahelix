package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.*;

class StandardRowSpecRowSourceFactoryTests {

    private static final Field field = new Field("Field 1");
    private static final ProfileFields fields = new ProfileFields(Collections.singletonList(field));

    @Test
    void shouldCreateMultiplexingRowSourceForRowSpec() {
        FieldSpec fieldSpec = FieldSpec.Empty;
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);
        FieldSpecValueGenerator valueGenerator = mock(FieldSpecValueGenerator.class);
        when(valueGenerator.generate(any(), any())).thenReturn(Stream.of(new Value(field, "value")));

        RowSourceFactory factory = new RowSourceFactory(valueGenerator);

        RowSource result = factory.createRowSource(rowSpec);

        Assert.assertThat(result, instanceOf(FieldCombiningRowSource.class));
    }

    @Test
    void shouldCreateValuesForEachFieldSpecInRowSpec() {
        FieldSpec fieldSpec = FieldSpec.Empty;
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);
        FieldSpecValueGenerator valueGenerator = mock(FieldSpecValueGenerator.class);
        when(valueGenerator.generate(any(), any())).thenReturn(Stream.of(new Value(field, "value")));

        RowSourceFactory factory = new RowSourceFactory(valueGenerator);

        factory.createRowSource(rowSpec);

        verify(valueGenerator, times(1)).generate(field, fieldSpec);
    }
}