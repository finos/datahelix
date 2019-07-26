package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.Field;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface HierarchicalFieldSpecGroup {

    List<Field> fields();

    Map<Field, FieldSpec> fieldSpecs();

    Collection<FieldSpecRelations> relations();

}
