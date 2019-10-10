package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.EqualToConstraintDTO;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import static com.scottlogic.deg.profile.reader.atomic.ConstraintReaderHelpers.getDateTimeGranularity;

public class RelationsFactory
{
    public static FieldSpecRelations create(AtomicConstraintDTO dto, ProfileFields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getDependency());
        switch (dto.getType())
        {
            case EQUAL_TO:
                Granularity offsetGranularity = getOffsetUnit(main.getType(), ((EqualToConstraintDTO) dto).offsetUnit);
                if (offsetGranularity != null)
                {
                    return new EqualToOffsetRelation(main, other, offsetGranularity, ((EqualToConstraintDTO) dto).offset);
                }
                return new EqualToRelation(main, other);
            case AFTER:
                return new AfterRelation(main, other, false, DateTimeDefaults.get());
            case AFTER_OR_AT:
                return new AfterRelation(main, other, true, DateTimeDefaults.get());
            case BEFORE:
                return new BeforeRelation(main, other, false, DateTimeDefaults.get());
            case BEFORE_OR_AT:
                return new BeforeRelation(main, other, true, DateTimeDefaults.get());
            case GREATER_THAN:
                return new AfterRelation(main, other, false, NumericDefaults.get());
            case GREATER_THAN_OR_EQUAL_TO:
                return new AfterRelation(main, other, true, NumericDefaults.get());
            case LESS_THAN:
                return new BeforeRelation(main, other, false, NumericDefaults.get());
            case LESS_THAN_OR_EQUAL_TO:
                return new BeforeRelation(main, other, true, NumericDefaults.get());
            default:
                throw new InvalidProfileException("Unexpected relation data type " + dto.getType());
        }
    }

    private static Granularity getOffsetUnit(FieldType type, String offsetUnit) {
        if (offsetUnit == null) return null;
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
