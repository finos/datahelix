package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

import java.time.OffsetDateTime;

public class BeforeOrEqualToDateRelation implements FieldSpecRelations {

    private final Field main;

    private final Field other;

    public BeforeOrEqualToDateRelation(Field main, Field other) {
        this.main = main;
        this.other = other;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        OffsetDateTime max = otherValue.getDateTimeRestrictions().max.getLimit();
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(max, true);

        return FieldSpec.Empty.withDateTimeRestrictions(restrictions);
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
