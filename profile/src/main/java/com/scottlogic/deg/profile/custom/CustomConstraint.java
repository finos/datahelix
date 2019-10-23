package com.scottlogic.deg.profile.custom;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.custom.CustomGenerator;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;

public class CustomConstraint implements AtomicConstraint {
    private final Field field;
    private final CustomGenerator customGenerator;
    private final boolean negated;

    public CustomConstraint(Field field, CustomGenerator customGenerator){
        this(field, customGenerator, false);
    }

    private CustomConstraint(Field field, CustomGenerator customGenerator, boolean negated) {
        this.field = field;
        this.customGenerator = customGenerator;
        this.negated = negated;

        if (!correctType()){
            throw new ValidationException(String.format("Custom generator %s requires type %s, but field %s is typed %s",
                customGenerator.generatorName(), customGenerator.fieldType(), field.name, field.getType()));
        }
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new CustomConstraint(field, customGenerator, !negated);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory
            .fromGenerator(
                new CustomFieldValueSource(customGenerator, negated),
                customGenerator::setMatchingFunction);
    }

    private boolean correctType() {
        switch (customGenerator.fieldType()) {
            case STRING:
                return field.getType() == FieldType.STRING;
            case DATETIME:
                return field.getType() == FieldType.DATETIME;
            case NUMERIC:
                return field.getType() == FieldType.NUMERIC;
            default:
                return false;
        }
    }
}
