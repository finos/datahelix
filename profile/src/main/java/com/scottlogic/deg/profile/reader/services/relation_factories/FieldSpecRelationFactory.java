package com.scottlogic.deg.profile.reader.services.relation_factories;

import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.RelationalConstraintDTO;

public abstract class FieldSpecRelationFactory
{
    public FieldSpecRelation createRelation(RelationalConstraintDTO dto, Fields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        DateTimeDefaults dateTimeDefaults = DateTimeDefaults.get();
        NumericDefaults numericDefaults = NumericDefaults.get();
        switch (dto.getType())
        {
            case EQUAL_TO_FIELD:
                return createEqualToRelation((EqualToFieldConstraintDTO) dto, fields);
            case AFTER_FIELD:
                return new AfterRelation(main, other, false, dateTimeDefaults);
            case AFTER_OR_AT_FIELD:
                return new AfterRelation(main, other, true, dateTimeDefaults);
            case BEFORE_FIELD:
                return new BeforeRelation(main, other, false, dateTimeDefaults);
            case BEFORE_OR_AT_FIELD:
                return new BeforeRelation(main, other, true, dateTimeDefaults);
            case GREATER_THAN_FIELD:
                return new AfterRelation(main, other, false, numericDefaults);
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                return new AfterRelation(main, other, true, numericDefaults);
            case LESS_THAN_FIELD:
                return new BeforeRelation(main, other, false, numericDefaults);
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                return new BeforeRelation(main, other, true, numericDefaults);
            default:
                throw new IllegalStateException("Unexpected relation type: " + dto.getType());
        }
    }

    abstract Granularity createGranularity(String offsetUnit);

    private FieldSpecRelation createEqualToRelation(EqualToFieldConstraintDTO dto, Fields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        Granularity granularity = createGranularity(dto.offsetUnit);
        return granularity == null
            ? new EqualToRelation(main, other)
            : new EqualToOffsetRelation(main, other, granularity, dto.offset);
    }

}
