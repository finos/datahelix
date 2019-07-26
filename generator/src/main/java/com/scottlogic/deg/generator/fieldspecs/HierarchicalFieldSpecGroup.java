package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class HierarchicalFieldSpecGroup {

    private final List<Field> fields;

    private final Map<Field, FieldSpec> fieldSpecs;

    private final Collection<FieldSpecRelations> relations;

    public HierarchicalFieldSpecGroup(List<Field> fields,
                                      Map<Field, FieldSpec> fieldSpecs,
                                      Collection<FieldSpecRelations> relations) {
        this.fields = fields;
        this.fieldSpecs = fieldSpecs;
        this.relations = relations;
    }

    List<Field> fields() {
        return fields;
    }

    Map<Field, FieldSpec> fieldSpecs() {
        return fieldSpecs;
    }

    Collection<FieldSpecRelations> relations() {
        return relations;
    }

}
