package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class RowSpecMerger {
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

    public RowSpec merge(RowSpec left, RowSpec right) {
        final Map<String, List<FieldSpec>> leftByName = getFieldsIndexedByName(left);
        final Map<String, List<FieldSpec>> rightByName = getFieldsIndexedByName(right);

        final Map<String, List<FieldSpec>> fieldNameToFields =
                Stream.concat(leftByName.entrySet().stream(), rightByName.entrySet().stream())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (leftValue, rightValue) -> Stream.concat(
                                        leftValue.stream(),
                                        rightValue.stream()
                                ).collect(Collectors.toList())
                        )
                );

        final List<FieldSpec> mergedFieldSpecs = fieldNameToFields.entrySet().stream()
                .map(x -> x.getValue().stream().reduce(
                        new FieldSpec(x.getKey()),
                        fieldSpecMerger::merge
                        )
                ).collect(Collectors.toList());

        return new RowSpec(mergedFieldSpecs);
    }

    private Map<String, List<FieldSpec>> getFieldsIndexedByName(RowSpec rowSpec) {
        return rowSpec.getFieldSpecs()
                .stream()
                .collect(Collectors.groupingBy(FieldSpec::getName));
    }
}
