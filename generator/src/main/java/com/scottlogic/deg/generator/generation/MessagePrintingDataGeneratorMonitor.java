package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessagePrintingDataGeneratorMonitor extends ReductiveDataGeneratorMonitor {

    public MessagePrintingDataGeneratorMonitor(PrintWriter writer) {
        this.writer = writer;
    }

    private void println(String message) {
        writer.println(message);
    }

    private void println(String message, Object... args) {
        writer.format(message, args);
        writer.println();
    }

    @Override
    public void rowEmitted(GeneratedObject item) {
        println("RowSpec emitted");
    }

    @Override
    public void fieldFixedToValue(Field field, Object current) {
        println("Field [%s] = %s", field.name, current);
    }

    @Override
    public void unableToStepFurther(ReductiveState reductiveState) {
        println(
            "%d: Unable to step further %s ",
            reductiveState.getFieldValues().size(),
            reductiveState.toString(true));
    }

    @Override
    public void noValuesForField(ReductiveState reductiveState, Field field) {
        println(
            "%d: No values for field %s: %s ",
            reductiveState.getFieldValues().size(),
            field,
            reductiveState.toString(true));
    }

    @Override
    public void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) {
        List<Map.Entry<Field, FieldSpec>> emptyFieldSpecs = fieldSpecsPerField.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == FieldSpec.Empty)
            .collect(Collectors.toList());

        println(
            "%d: Unable to emit row, some FieldSpecs are Empty: %s",
            reductiveState.getFieldValues().size(),
            Objects.toString(emptyFieldSpecs));
    }
}
