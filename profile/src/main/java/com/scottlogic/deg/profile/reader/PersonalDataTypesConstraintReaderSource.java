package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInNameSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.profile.reader.file.inputstream.ClasspathMapper;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalDataTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {

    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        ConstraintReader nameConstraintReader = (dto, fields, rules) -> {
            NameConstraintTypes type = lookupNameConstraint(dto);
            NameRetriever nameRetriever = new NameRetriever(new ClasspathMapper());

            Set<Object> objects = Stream.of(type)
                .map(nameRetriever)
                .map(ConstraintReaderHelpers::downcastToObject)
                .collect(Collectors.toSet());

            Field field = fields.getByName(dto.field);
            return new AndConstraint(
                new IsInNameSetConstraint(field, objects, rules),
                new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING, rules)
            );
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
