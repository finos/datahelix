package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;

import java.util.Collection;
import java.util.Map;

public final class FieldSpecGroup {

    private final Map<Field, FieldSpec> fieldSpecs;

    private final Collection<FieldSpecRelations> relations;

    public FieldSpecGroup(Map<Field, FieldSpec> fieldSpecs, Collection<FieldSpecRelations> relations) {
        this.fieldSpecs = fieldSpecs;
        this.relations = relations;
    }

    public Map<Field, FieldSpec> fieldSpecs() {
        return fieldSpecs;
    };

    public Collection<FieldSpecRelations> relations() {
        return relations;
    };

}
