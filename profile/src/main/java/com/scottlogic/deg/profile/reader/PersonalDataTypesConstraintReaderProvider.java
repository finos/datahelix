package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInNameSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.profile.reader.names.NameCSVPopulator;
import com.scottlogic.deg.profile.reader.names.NameHolder;
import com.scottlogic.deg.profile.reader.names.NameRetrievalService;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalDataTypesConstraintReaderProvider implements ConstraintReaderMapEntryProvider {

    private final NameRetrievalService nameRetrievalService;

    public PersonalDataTypesConstraintReaderProvider() {
        nameRetrievalService = new NameRetrievalService(new NameCSVPopulator());
    }

    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        ConstraintReader nameConstraintReader = (dto, fields, rules) -> {
            NameConstraintTypes type = lookupNameConstraint(dto);
            Set<Object> objects = nameRetrievalService.retrieveValues(type)
                .stream()
                .map(NameHolder::getName)
                .map(Object.class::cast)
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
