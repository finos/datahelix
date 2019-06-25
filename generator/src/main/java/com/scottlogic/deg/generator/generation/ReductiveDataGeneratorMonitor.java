package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ReductiveDataGeneratorMonitor implements DataGeneratorMonitor {
    protected PrintWriter writer;
    private List<String> linesToPrintAtEndOfGeneration = new ArrayList<>();

    public void endGeneration() {
        for (String line : linesToPrintAtEndOfGeneration) {
            writer.println(line);
        }
    }

    public void addLineToPrintAtEndOfGeneration(String line) {
        linesToPrintAtEndOfGeneration.add(line);
    }

    public void fieldFixedToValue(Field field, Object current) {}
    public void unableToStepFurther(ReductiveState reductiveState) {}
    public void noValuesForField(ReductiveState reductiveState, Field field) {}
    public void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) {}
}
