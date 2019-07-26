package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.Field;

import java.util.Collection;
import java.util.Map;

public interface FieldSpecGroup {

    Map<Field, FieldSpec> fieldSpecs();

    Collection<FieldSpecRelations> relations();

}
