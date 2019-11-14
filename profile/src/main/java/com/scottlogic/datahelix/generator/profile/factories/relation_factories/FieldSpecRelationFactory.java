/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.profile.factories.relation_factories;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.profile.Granularity;
import com.scottlogic.datahelix.generator.common.util.defaults.DateTimeDefaults;
import com.scottlogic.datahelix.generator.common.util.defaults.NumericDefaults;
import com.scottlogic.datahelix.generator.core.fieldspecs.relations.*;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.RelationalConstraintDTO;

import static com.scottlogic.datahelix.generator.common.util.GranularityUtils.readGranularity;

public abstract class FieldSpecRelationFactory
{
    public FieldSpecRelation createRelation(RelationalConstraintDTO dto, Fields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        if(dto.getType() == ConstraintType.EQUAL_TO_FIELD)
        {
            return createEqualToRelation((EqualToFieldConstraintDTO) dto, fields);
        }
        Granularity offsetGranularity = readGranularity(main.getType(), dto.offsetUnit);
        DateTimeDefaults dateTimeDefaults = DateTimeDefaults.get();
        NumericDefaults numericDefaults = NumericDefaults.get();
        switch (dto.getType())
        {
            case AFTER_FIELD:
                return new AfterRelation(main, other, dto.offset > 0, dateTimeDefaults, offsetGranularity, dto.offset);
            case AFTER_OR_AT_FIELD:
                return new AfterRelation(main, other, true, dateTimeDefaults, offsetGranularity, 0);
            case BEFORE_FIELD:
                return new BeforeRelation(main, other, dto.offset > 0, dateTimeDefaults, offsetGranularity, dto.offset);
            case BEFORE_OR_AT_FIELD:
                return new BeforeRelation(main, other, true, dateTimeDefaults, offsetGranularity, 0);
            case GREATER_THAN_FIELD:
                return new AfterRelation(main, other, false, numericDefaults, offsetGranularity, 0);
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                return new AfterRelation(main, other, true, numericDefaults, offsetGranularity, 0);
            case LESS_THAN_FIELD:
                return new BeforeRelation(main, other, false, numericDefaults, offsetGranularity, 0);
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                return new BeforeRelation(main, other, true, numericDefaults, offsetGranularity, 0);
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
