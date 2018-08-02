package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A complete set of information needed to generate a row satisfying a set of constraints.
 *
 * Typically created by combining choices over a decision tree.
 */
public class RowSpec {
    private final List<Field> fields;
    private final Map<Field, FieldSpec> fieldToFieldSpec;

    public RowSpec(
        List<Field> fields,
        Map<Field, FieldSpec> fieldToFieldSpec) {

        this.fields = fields;
        this.fieldToFieldSpec = fieldToFieldSpec;
    }

    public FieldSpec getSpecForField(Field field) {
        FieldSpec ownFieldSpec = this.fieldToFieldSpec.get(field);

        if (ownFieldSpec == null)
            return FieldSpec.Empty;

        return ownFieldSpec;
    }

    public static RowSpec merge(
        FieldSpecMerger fieldSpecMerger,
        RowSpec... rowSpecsToMerge) {

        List<Field> fields = rowSpecsToMerge[0].fields;

        Map<Field, FieldSpec> fieldToFieldSpec = new HashMap<>();
        for (Field field : fields) {
            fieldToFieldSpec.put(
                field,
                Arrays.stream(rowSpecsToMerge)
                    .map(rowSpec -> rowSpec.getSpecForField(field))
                    .reduce(
                        new FieldSpec(),
                        fieldSpecMerger::merge));
        }

        return new RowSpec(fields, fieldToFieldSpec);
    }
}
