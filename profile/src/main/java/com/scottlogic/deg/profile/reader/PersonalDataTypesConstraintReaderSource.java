package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInNameSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.util.HeterogeneousTypeContainer;
import com.scottlogic.deg.profile.reader.names.NameCSVPopulator;
import com.scottlogic.deg.profile.reader.names.NameHolder;
import com.scottlogic.deg.profile.reader.names.NameRetrievalService;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalDataTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {
    private final HeterogeneousTypeContainer<CatalogService<?, ?>> catalogServices;

    public PersonalDataTypesConstraintReaderSource() {
        catalogServices = new HeterogeneousTypeContainer<CatalogService<?, ?>>().put(
            NameRetrievalService.class,
            new NameRetrievalService(new NameCSVPopulator())
        );
    }

    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        ConstraintReader nameConstraintReader = (dto, fields, rules) -> {
            NameConstraintTypes type = NameConstraintTypes.lookupProfileText(ConstraintReaderHelpers.getValidatedValue(dto, String.class));
            Field field = fields.getByName(dto.field);

            NameRetrievalService service = catalogServices.get(NameRetrievalService.class)
                .orElseThrow(() -> new UnsupportedOperationException("No name retrieval service set!"));

            Set<Object> objects = service.retrieveValues(type)
                .stream()
                .map(NameHolder::getName)
                .map(Object.class::cast)
                .collect(Collectors.toSet());

            return new AndConstraint(
                new IsInNameSetConstraint(field, objects, rules),
                new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING, rules)
            );
        };

        return Arrays.stream(NameConstraintTypes.values())
            .map(n -> new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                n.getProfileText(),
                nameConstraintReader
            ));
    }
}
