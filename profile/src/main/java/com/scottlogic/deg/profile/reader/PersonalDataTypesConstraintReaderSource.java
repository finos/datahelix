package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInNameSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
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
            Set<Object> names = NameRetriever.loadNamesFromFile(type);

            Field field = fields.getByName(dto.field);

            return new IsInNameSetConstraint(field, names, rules);
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
