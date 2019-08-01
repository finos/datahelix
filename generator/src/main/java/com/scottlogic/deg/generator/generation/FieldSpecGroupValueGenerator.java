package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.*;
import java.util.stream.Stream;

public class FieldSpecGroupValueGenerator {

    private final FieldSpecValueGenerator underlyingGenerator;

    public FieldSpecGroupValueGenerator(FieldSpecValueGenerator underlyingGenerator) {
        this.underlyingGenerator = underlyingGenerator;
    }

    public Stream<DataBag> generate(FieldSpecGroup group) {
        Map<Field, FieldSpec> fields = group.fieldSpecs();

        Collection<FieldSpecRelations> relations = group.relations();

        Field first = fields.keySet().iterator().next();

        // Ensure bounds are re-calculated at EACH STEP.
        // recalc bounds initially
        FieldSpecGroup groupRespectingFirstField = adjustBounds(first, group);
        FieldSpec firstSpec = groupRespectingFirstField.fieldSpecs().get(first);
        Stream<DataBag> firstDataBagValues = underlyingGenerator.generate(firstSpec)
            .map(value -> toDataBag(first, value));

        return createRemainingDataBags(firstDataBagValues, groupRespectingFirstField);

        // recursively operate on stream, adding
    }

    private DataBag toDataBag(Field field, DataBagValue value) {
        Map<Field, DataBagValue> map = new HashMap<>();
        map.put(field, value);
        return new DataBag(map);
    }

    private FieldSpecGroup adjustBounds(Field field, FieldSpecGroup group) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    private Stream<DataBag> createRemainingDataBags(Stream<DataBag> stream, FieldSpecGroup group) {
        throw new UnsupportedOperationException("Not implemented!");
    }

}
