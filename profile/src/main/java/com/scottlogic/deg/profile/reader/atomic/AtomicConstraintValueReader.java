package com.scottlogic.deg.profile.reader.atomic;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtomicConstraintValueReader {

    private final FromFileReader fromFileReader;

    @Inject
    public AtomicConstraintValueReader(FromFileReader fromFileReader) {
        this.fromFileReader = fromFileReader;
    }


    public Object getValue(ConstraintDTO dto){
        try {
            return tryGetValue(dto);
        } catch (IllegalArgumentException | ValidationException e){
            throw new InvalidProfileException(String.format("Field [%s]: %s", dto.field, e.getMessage()));
        }
    }

    public Object tryGetValue(ConstraintDTO dto){
        if (dto.values != null){
            return getSet(dto.values);
        }

        if (dto.file != null){
            return fromFileReader.setFromFile(dto.file);
        }

        return getValue(dto.value);
    }

    private DistributedSet getSet(Collection<Object> values) {
        List collect = values.stream()
            .map(val -> getValue(val))
            .collect(Collectors.toList());
        return DistributedSet.uniform(collect);
    }

    private Object getValue(Object value) {
        if (value instanceof Map){
            return getDate((Map) value);
        }

        return value;
    }


    private OffsetDateTime getDate(Map value) {
        return ConstraintReaderHelpers.parseDate((String) (value).get("date"));
    }
}
