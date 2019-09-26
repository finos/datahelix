package com.scottlogic.deg.generator.generation.grouped;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.FieldWithFieldSpec;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldSpecGroupDateHelper {

    public static FieldSpecGroup adjustBoundsOfDate(Field field, OffsetDateTime value, FieldSpecGroup group) {

        Limit<OffsetDateTime> limit = new Limit<>(value, true);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(limit,limit);
        FieldSpec newSpec = FieldSpec.empty().withNotNull().withRestrictions(restrictions);

        return adjustBoundsOfDateFromFieldSpec(field, newSpec, group);
    }

    private static FieldSpecGroup adjustBoundsOfDateFromFieldSpec(Field field, FieldSpec newSpec, FieldSpecGroup group) {

        Map<Field, FieldSpec> specs = new HashMap<>(group.fieldSpecs());
        specs.replace(field, newSpec);

        Set<FieldSpecRelations> relations = group.relations().stream()
            .filter(relation -> relation.main().equals(field) || relation.other().equals(field))
            .collect(Collectors.toSet());
        Stream<FieldWithFieldSpec> relationsOrdered = relations.stream()
            .map(relation -> relation.main().equals(field) ? relation : relation.inverse())
            .map(relation -> new FieldWithFieldSpec(relation.other(), relation.reduceToRelatedFieldSpec(newSpec)));

        relationsOrdered.forEach(
            wrapper -> applyToFieldSpecMap(
                specs,
                specs.get(wrapper.field()),
                wrapper.fieldSpec(),
                wrapper.field()));
        return new FieldSpecGroup(specs, relations);
    }

    private static void applyToFieldSpecMap(Map<Field, FieldSpec> map, FieldSpec left, FieldSpec right, Field field) {
        FieldSpecMerger merger = new FieldSpecMerger();

        FieldSpec newSpec = merger.merge(left, right)
            .orElseThrow(() -> new IllegalArgumentException("Failed to create field spec from value"));
        map.put(field, newSpec);
    }
}
