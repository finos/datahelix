package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.fieldspecs.BaseFieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;

public class NullOnlyFieldSpec extends BaseFieldSpec {
    NullOnlyFieldSpec() {
        super(true);
    }

    @Override
    public boolean permits(Object value) {
        return false;
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return new NullOnlySource();
    }

    @Override
    public FieldSpec withNotNull() {
        throw new UnsupportedOperationException("not null on null only not allowed");
    }
}
