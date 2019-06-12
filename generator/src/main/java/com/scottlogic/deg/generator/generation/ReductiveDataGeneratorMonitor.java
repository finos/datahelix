package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.util.LinkedList;
import java.util.Map;

public abstract class ReductiveDataGeneratorMonitor implements DataGeneratorMonitor {
    private LinkedList<String> linesToPrintAtEndOfGeneration = new LinkedList<>();
    public void endGeneration() {
        while (linesToPrintAtEndOfGeneration.size() > 0) {
            System.err.println(linesToPrintAtEndOfGeneration.removeLast());
        }
    }
    public void addLineToPrintAtEndOfGeneration(String line) {
        linesToPrintAtEndOfGeneration.push(line);
    }
    public void fieldFixedToValue(Field field, Object current) {}
    public void unableToStepFurther(ReductiveState reductiveState) {}
    public void noValuesForField(ReductiveState reductiveState, Field field) {}
    public void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) {}
}
