package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;

import java.util.Collections;

public class NotEqualToRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;

    public NotEqualToRelation(Field main, Field other) {
        this.main = main;
        this.other = other;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        return FieldSpecFactory.fromType(main.getType());
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        return FieldSpecFactory.fromType(main.getType()).withBlacklist(Collections.singleton(generatedValue.getValue()));
    }

    @Override
    public FieldSpecRelations inverse() {
        return new NotEqualToRelation(other, main);
    }

    @Override
    public Field main() {
        return main;
    }

    @Override
    public Field other() {
        return other;
    }

    @Override
    public Constraint negate() {
        return new EqualToRelation(main, other);
    }
}
