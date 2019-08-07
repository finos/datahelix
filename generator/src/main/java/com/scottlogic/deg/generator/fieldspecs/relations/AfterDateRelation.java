package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;


public class AfterDateRelation extends AbstractDateInequalityRelation {

    public AfterDateRelation(Field main, Field other) {
        super(main, other);
    }

    @Override
    protected DateTimeRestrictions.DateTimeLimit dateTimeLimitExtractingFunction(DateTimeRestrictions restrictions) {
        return restrictions.max;
    }

    @Override
    public FieldSpecRelations inverse() {
        return new BeforeDateRelation(main(), other());
    }

}
