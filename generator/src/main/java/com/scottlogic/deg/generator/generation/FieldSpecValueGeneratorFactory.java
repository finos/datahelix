package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;

import java.util.stream.Stream;

public class FieldSpecValueGeneratorFactory {
    private final FieldValueSourceEvaluator evaluator;

    @Inject
    public FieldSpecValueGeneratorFactory(FieldValueSourceEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public DataBagSource getFieldSpecValueGenerator(Field field, FieldSpec fieldSpec){
        return new FieldSpecValueGeneratorInverter(field, fieldSpec, evaluator);
    }

    class FieldSpecValueGeneratorInverter implements DataBagSource {
        private final FieldSpec fieldSpec;
        private final Field field;
        private final FieldValueSourceEvaluator evaluator;

        FieldSpecValueGeneratorInverter(
            Field field,
            FieldSpec fieldSpec,
            FieldValueSourceEvaluator evaluator) {
            this.fieldSpec = fieldSpec;
            this.field = field;
            this.evaluator = evaluator;
        }

        @Override
        public Stream<DataBag> generate(GenerationConfig generationConfig) {
            return new FieldSpecValueGenerator(generationConfig, this.evaluator)
                .generate(this.field, this.fieldSpec);
        }
    }
}
