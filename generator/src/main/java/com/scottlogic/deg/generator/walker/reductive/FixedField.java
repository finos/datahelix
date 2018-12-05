package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

public class FixedField {
    private static final Object NOT_ITERATED = new NotIterated();

    public final Field field;

    private final Stream<Object> values;
    private final FieldSpec valuesFieldSpec;
    private final ReductiveDataGeneratorMonitor monitor;

    private Object current = NOT_ITERATED;
    private FieldSpec fieldSpec;

    FixedField(
        Field field,
        Stream<Object> values,
        FieldSpec valuesFieldSpec,
        ReductiveDataGeneratorMonitor monitor) {
        this.field = field;
        this.values = values;
        this.valuesFieldSpec = valuesFieldSpec;
        this.monitor = monitor;
    }

    public Stream<Object> getStream() {
        return this.values
            .peek(value -> {
                this.current = value;
                this.fieldSpec = null;

                this.monitor.fieldFixedToValue(this.field, this.current);
            });
    }

    public FieldSpec getFieldSpecForValues(){
        return this.valuesFieldSpec;
    }

    @Override
    public String toString() {
        return this.current == NOT_ITERATED
            ? this.field.name
            : String.format("[%s] = %s", this.field.name, this.current);
    }

    FieldSpec getFieldSpecForCurrentValue(){
        if (this.fieldSpec != null) {
            return this.fieldSpec;
        }

        return this.fieldSpec = current == null
            ? getNullRequiredFieldSpec()
            : getFieldSpecForCurrentValue(current);
    }

    private FieldSpec getFieldSpecForCurrentValue(Object currentValue) {
        if (currentValue == NOT_ITERATED){
            throw new UnsupportedOperationException("FixedField has not iterated yet");
        }

        return FieldSpec.Empty.withSetRestrictions(
            new SetRestrictions(new HashSet<>(Collections.singletonList(currentValue)), null),
            this.valuesFieldSpec.getFieldSpecSource()
        );
    }

    private FieldSpec getNullRequiredFieldSpec() {
        return FieldSpec.Empty
        .withNullRestrictions(
            new NullRestrictions(NullRestrictions.Nullness.MUST_BE_NULL),
            this.valuesFieldSpec.getFieldSpecSource()
        );
    }

    private static class NotIterated { }
}
