package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

class BeforeDateRelation extends AbstractDateInequalityRelation {

    public BeforeDateRelation(Field main, Field other) {
        super(main, other);
    }

    @Override
    public DateTimeRestrictions.DateTimeLimit dateTimeLimitExtractingFunction(DateTimeRestrictions restrictions) {
        return restrictions.min;
    }

    @Override
    public FieldSpecRelations inverse() {
        return new AfterDateRelation(other(), main());
    }
}
