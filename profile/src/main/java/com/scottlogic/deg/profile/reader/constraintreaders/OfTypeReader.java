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

package com.scottlogic.deg.profile.reader.constraintreaders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.profile.reader.ConstraintReader;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import com.scottlogic.deg.profile.dto.ConstraintDTO;

import java.math.BigDecimal;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.*;

public class OfTypeReader implements ConstraintReader {

    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
        Field field = fields.getByName(dto.field);

        String value = ConstraintReaderHelpers.getValidatedValue(dto, String.class);
        switch (value) {
            case "decimal":
                return new IsOfTypeConstraint(field, NUMERIC);

            case "string":
                return new IsOfTypeConstraint(field, STRING);

            case "datetime":
                return new IsOfTypeConstraint(field, DATETIME);

            case "integer":
                return new AndConstraint(
                    new IsOfTypeConstraint(field, NUMERIC),
                    new IsGranularToNumericConstraint(field, new ParsedGranularity(BigDecimal.ONE)));

            case "ISIN":
            case "SEDOL":
            case "CUSIP":
            case "RIC":
                return new AndConstraint(
                    new MatchesStandardConstraint(field, StandardConstraintTypes.valueOf(value)),
                    new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING)
                );
        }

        try {
            NameConstraintTypes nameType = NameConstraintTypes.lookupProfileText(value);
            DistributedSet<Object> objectDistributedSet = NameRetriever.loadNamesFromFile(nameType);
            return new IsInSetConstraint(field, objectDistributedSet);
        } catch (UnsupportedOperationException e){
            throw new InvalidProfileException("Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"" + value + "\"");
        }
    }
}
