package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ReductiveRowSpec extends RowSpec {
    final Field lastFixedField;

    public ReductiveRowSpec(ProfileFields fields, Map<Field, FieldSpec> fieldToFieldSpec, Field lastFixedField) {
        super(fields, fieldToFieldSpec);
        this.lastFixedField = lastFixedField;
    }
}
