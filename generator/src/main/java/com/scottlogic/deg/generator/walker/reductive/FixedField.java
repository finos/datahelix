package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecHelper;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

public class FixedField {
    private static final Object NOT_ITERATED = new NotIterated();

    private final Field field;
    private final Stream<Object> values;
    private final ReductiveDataGeneratorMonitor monitor;

    private Object current = NOT_ITERATED;

    FixedField(
        Field field,
        Stream<Object> values,
        ReductiveDataGeneratorMonitor monitor) {
        this.field = field;
        this.values = values;
        this.monitor = monitor;
    }

    public Field getField(){
        return field;
    }

    public Stream<Object> getStream() {
        return this.values
            .peek(value -> {
                this.current = value;

                this.monitor.fieldFixedToValue(this.field, this.current);
            });
    }

    @Override
    public String toString() {
        return this.current == NOT_ITERATED
            ? this.field.name
            : String.format("[%s] = %s", this.field.name, this.current);
    }

    FieldSpec getFieldSpecForCurrentValue(){
        return FieldSpecHelper.getFieldSpecForCurrentValue(current);
    }

    public Object getCurrentValue() {
        if (this.current == NOT_ITERATED){
            throw new UnsupportedOperationException("FixedField has not iterated yet");
        }

        return this.current;
    }

    private static class NotIterated { }
}
