package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

import java.time.OffsetDateTime;

public class BeforeDateRelation implements FieldSpecRelations {

    private final Field main;

    private final Field other;

    public BeforeDateRelation(Field main, Field other) {
        this.main = main;
        this.other = other;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        DateTimeRestrictions.DateTimeLimit minLimit = otherValue.getDateTimeRestrictions().min;

        if (minLimit != null) {
            OffsetDateTime min = minLimit.getLimit();

            DateTimeRestrictions restrictions = new DateTimeRestrictions();
            restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, false);

            return FieldSpec.Empty.withDateTimeRestrictions(restrictions);
        } else {
            return FieldSpec.Empty;
        }
    }

    @Override
    public FieldSpecRelations inverse() {
        return new AfterDateRelation(other, main);
    }

    @Override
    public Field main() {
        return main;
    }

    @Override
    public Field other() {
        return other;
    }
}
