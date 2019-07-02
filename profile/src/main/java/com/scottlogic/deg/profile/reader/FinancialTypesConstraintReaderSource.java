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

package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.MatchesStandardConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.util.stream.Stream;

public class FinancialTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {
    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        ConstraintReader financialCodesReader = (dto, fields, rules) -> {
            StandardConstraintTypes standardType =
                StandardConstraintTypes.valueOf(
                    ConstraintReaderHelpers.getValidatedValue(dto, String.class)
                );
            Field field = fields.getByName(dto.field);
            switch (standardType) {
                case ISIN:
                case SEDOL:
                case CUSIP:
                case RIC:
                    return new AndConstraint(
                        new MatchesStandardConstraint(field, standardType),
                        new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING)
                    );
                default:
                    return new MatchesStandardConstraint(field, standardType);
            }
        };

        return Stream.of(
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "ISIN",
                financialCodesReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "SEDOL",
                financialCodesReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "CUSIP",
                financialCodesReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "RIC",
                financialCodesReader
            )
        );
    }
}
