package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.*;

class FieldSpecValueGeneratorFactoryTests {

    @Test
    void shouldReturnFieldSpecValueGeneratorInverterAsDataBagSource() {
        FieldValueSourceEvaluator evaluator = mock(FieldValueSourceEvaluator.class);
        FieldSpecValueGeneratorFactory factory = new FieldSpecValueGeneratorFactory(evaluator);
        Field field = new Field("field 1");
        FieldSpec fieldSpec = FieldSpec.Empty;

        DataBagSource result = factory.getFieldSpecValueGenerator(field, fieldSpec);

        Assert.assertThat(result, instanceOf(FieldSpecValueGeneratorFactory.FieldSpecValueGeneratorAdapter.class));
    }

    @Test
    void shouldUseEvaluatorDependencyToProduceValues() {
        FieldValueSourceEvaluator evaluator = mock(FieldValueSourceEvaluator.class);
        FieldSpecValueGeneratorFactory factory = new FieldSpecValueGeneratorFactory(evaluator);
        Field field = new Field("field 1");
        FieldSpec fieldSpec = FieldSpec.Empty;
        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource() {{ this.generationType = GenerationConfig.DataGenerationType.INTERESTING; }}
        );
        when(evaluator.getFieldValueSources(fieldSpec)).thenReturn(Collections.emptyList());

        DataBagSource result = factory.getFieldSpecValueGenerator(field, fieldSpec);
        result.generate(generationConfig);

        verify(evaluator, times(1)).getFieldValueSources(fieldSpec);
    }
}