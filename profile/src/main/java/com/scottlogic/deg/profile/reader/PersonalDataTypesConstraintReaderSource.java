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
import com.scottlogic.deg.common.profile.constraints.atomic.IsInNameSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class PersonalDataTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {

    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        ConstraintReader nameConstraintReader = (dto, fields, rules) -> {

            NameConstraintTypes type = lookupNameConstraint(dto);
            Set<ElementFrequency<Object>> names = NameRetriever.loadNamesFromFile(type);

            Field field = fields.getByName(dto.field);

            return new IsInNameSetConstraint(field, names);
        };

        return Arrays.stream(NameConstraintTypes.values())
            .map(nameType -> new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                nameType.getProfileText(),
                nameConstraintReader
            ));
    }

    private NameConstraintTypes lookupNameConstraint(ConstraintDTO dto) {
        return NameConstraintTypes.lookupProfileText(ConstraintReaderHelpers.getValidatedValue(dto, String.class));
    }
}
