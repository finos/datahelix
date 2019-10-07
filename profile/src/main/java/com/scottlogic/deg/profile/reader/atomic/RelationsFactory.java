package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Types;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.generator.restrictions.linear.NumericGranularity;
import com.scottlogic.deg.profile.dto.ConstraintDTO;

import static com.scottlogic.deg.profile.reader.atomic.ConstraintReaderHelpers.getDateTimeGranularity;

public class RelationsFactory {
    public static FieldSpecRelations create(ConstraintDTO dto, ProfileFields fields){
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.otherField);

        if (main.type != other.type){
            throw new ValidationException("Field " + main.name + " cannot be related to other field " + other.name);
        }

        Granularity offsetGranularity = getOffsetUnit(main.type, dto.offsetUnit);

        switch (AtomicConstraintType.fromText((String) dto.is)) {
            case IS_EQUAL_TO_CONSTANT:
                if (offsetGranularity != null){
                    return new EqualToOffsetRelation(main, other, offsetGranularity, dto.offset);
                }
                return new EqualToRelation(main, other);

            case IS_AFTER_CONSTANT_DATE_TIME:
                return new AfterDateRelation(main, other, false);
            case IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new AfterDateRelation(main, other, true);
            case IS_BEFORE_CONSTANT_DATE_TIME:
                return new BeforeDateRelation(main, other, false);
            case IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new BeforeDateRelation(main, other, true);
        }

        throw new ValidationException(dto.is + "cannot be used with OtherValue)");
    }

    private static Granularity getOffsetUnit(Types type, String offsetUnit) {
        if (offsetUnit == null) {
            return null;
        }

        switch (type) {
            case NUMERIC:
                return new NumericGranularity(ParsedGranularity.parse(offsetUnit).getNumericGranularity().scale());
            case DATETIME:
                return getDateTimeGranularity(offsetUnit);
            default:
                return null;
        }
    }
}
