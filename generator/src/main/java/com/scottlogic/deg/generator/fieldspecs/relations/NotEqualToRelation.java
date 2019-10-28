package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.fields.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.common.profile.rules.constraints.Constraint;

import java.util.Collections;

public class NotEqualToRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;

    public NotEqualToRelation(Field main, Field other) {
        this.main = main;
        this.other = other;
    }

    @Override
    public FieldSpec createModifierFromOtherFieldSpec(FieldSpec otherFieldSpec) {
        return FieldSpecFactory.fromType(main.getType());
    }

    @Override
    public FieldSpec createModifierFromOtherValue(DataBagValue otherFieldGeneratedValue) {
        return FieldSpecFactory.fromType(main.getType()).withBlacklist(Collections.singleton(otherFieldGeneratedValue.getValue()));
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
