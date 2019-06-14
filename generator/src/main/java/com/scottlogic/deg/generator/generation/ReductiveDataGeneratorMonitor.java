package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ReductiveDataGeneratorMonitor implements DataGeneratorMonitor {
    private List<LinkedHashMap<String, PrintStream>> linesToPrintAtEndOfGeneration = new ArrayList<>();
    public void endGeneration() {
        for (LinkedHashMap<String, PrintStream> pair : linesToPrintAtEndOfGeneration) {
            pair.forEach((String line, PrintStream out) -> out.println(line));
        }
    }

    public void addLineToPrintAtEndOfGeneration(String line, PrintStream pw) {
        LinkedHashMap<String, PrintStream> pair = new LinkedHashMap<>();
        pair.put(line, pw);
        linesToPrintAtEndOfGeneration.add(pair);
    }
    public void fieldFixedToValue(Field field, Object current) {}
    public void unableToStepFurther(ReductiveState reductiveState) {}
    public void noValuesForField(ReductiveState reductiveState, Field field) {}
    public void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) {}
}
