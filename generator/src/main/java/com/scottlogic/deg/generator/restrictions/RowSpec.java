package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A complete set of information needed to generate a row satisfying a set of constraints.
 *
 * Typically created by combining choices over a decision tree.
 */
public class RowSpec {
    private final ProfileFields fields;
    private final Map<Field, FieldSpec> fieldToFieldSpec;

    public RowSpec(
        ProfileFields fields,
        Map<Field, FieldSpec> fieldToFieldSpec) {

        this.fields = fields;
        this.fieldToFieldSpec = fieldToFieldSpec;
    }

    public ProfileFields getFields() {
        return fields;
    }

    public FieldSpec getSpecForField(Field field) {
        FieldSpec ownFieldSpec = this.fieldToFieldSpec.get(field);

        if (ownFieldSpec == null)
            return FieldSpec.Empty;

        return ownFieldSpec;
    }

    public static Optional<RowSpec> merge(
            FieldSpecMerger fieldSpecMerger,
            Collection<RowSpec> rowSpecs
    ) {
        if (rowSpecs.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        final ProfileFields fields = rowSpecs.iterator().next().fields;

        final Map<Field, Optional<FieldSpec>> fieldToFieldSpec = fields
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                field -> rowSpecs
                                        .stream()
                                        .map(x -> x.getSpecForField(field))
                                        .reduce(
                                                Optional.of(FieldSpec.Empty),
                                                (acc, next) -> acc.flatMap(fieldSpec -> fieldSpecMerger.merge(fieldSpec, next)),
                                                (opt1, opt2) -> opt1.flatMap(
                                                        fieldSpec1 -> opt2.flatMap(
                                                                fieldSpec2 -> fieldSpecMerger.merge(fieldSpec1, fieldSpec2)
                                                        )
                                                )
                                        )
                        )
                );

        final Optional<Map<Field, FieldSpec>> optFieldToFieldSpec = Optional.of(fieldToFieldSpec)
                .filter(map -> map.values().stream().allMatch(Optional::isPresent))
                .map(map -> map
                        .entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> entry.getValue().get()
                                )
                        )
                );

        return optFieldToFieldSpec.map(
                map -> new RowSpec(fields, map)
        );
    }

    @Override
    public String toString() {
        return Objects.toString(fieldToFieldSpec);
    }

    public IDataBagSource createDataBagSource() {
        List<IDataBagSource> fieldDataBagSources = new ArrayList<>(fields.size());

        for (Field field : fields) {
            FieldSpec fieldSpec = getSpecForField(field);

            fieldDataBagSources.add(
                new FieldSpecFulfiller(field, fieldSpec));
        }

        return new MultiplexingDataBagSource(fieldDataBagSources.stream());
    }
}
