package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.profile.dto.ConstraintDTO;

import static com.scottlogic.deg.profile.reader.atomic.ConstraintReaderHelpers.getDateTimeGranularity;

public class RelationsFactory {
    public static FieldSpecRelations create(ConstraintDTO dto, ProfileFields fields){
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.otherField);

        if (main.getType() != other.getType()){
            throw new ValidationException("Field " + main.name + " cannot be related to other field " + other.name);
        }

        Granularity offsetGranularity = getOffsetUnit(main.getType(), dto.offsetUnit);

        switch (AtomicConstraintType.fromText((String) dto.is)) {
            case IS_EQUAL_TO_CONSTANT:
                if (offsetGranularity != null){
                    return new EqualToOffsetRelation(main, other, offsetGranularity, dto.offset);
                }
                return new EqualToRelation(main, other);

            case IS_AFTER_CONSTANT_DATE_TIME:
                return new AfterRelation(main, other, false, DateTimeDefaults.get());
            case IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new AfterRelation(main, other, true, DateTimeDefaults.get());
            case IS_BEFORE_CONSTANT_DATE_TIME:
                return new BeforeRelation(main, other, false, DateTimeDefaults.get());
            case IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new BeforeRelation(main, other, true, DateTimeDefaults.get());

            case IS_GREATER_THAN_CONSTANT:
                return new AfterRelation(main, other, false, NumericDefaults.get());
            case IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT:
                return new AfterRelation(main, other, true, NumericDefaults.get());
            case IS_LESS_THAN_CONSTANT:
                return new BeforeRelation(main, other, false, NumericDefaults.get());
            case IS_LESS_THAN_OR_EQUAL_TO_CONSTANT:
                return new BeforeRelation(main, other, true, NumericDefaults.get());
        }

        throw new ValidationException(dto.is + "cannot be used with OtherValue)");
    }

    private static Granularity getOffsetUnit(FieldType type, String offsetUnit) {
        if (offsetUnit == null) {
            return null;
        }

        switch (type) {
            case NUMERIC:
                return NumericGranularityFactory.create(offsetUnit);
            case DATETIME:
                return getDateTimeGranularity(offsetUnit);
            default:
                return null;
        }
    }
}
