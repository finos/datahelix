package com.scottlogic.deg.profile.reader.atomic;

import com.google.inject.Inject;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;

import java.math.BigDecimal;
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
        if (dto.values != null){
            return getSet(dto.values, dto);
        }

        if (dto.file != null){
            return fromFileReader.setFromFile(dto.file);
        }

        return getValue(dto.value, dto);
    }

    private FrequencyDistributedSet getSet(Collection<Object> values, ConstraintDTO dto) {
        if (!(values instanceof Collection)){
            return null;//TODO lul
        }

        List collect = values.stream()
            .map(val -> getValue(val, dto))
            .collect(Collectors.toList());
        return FrequencyDistributedSet.uniform(collect);
    }

    private Object getValue(Object value, ConstraintDTO dto) {
        if (value instanceof Map){
            return getDate((Map) value, dto);
        }

        return value;
    }


    private OffsetDateTime getDate(Map value, ConstraintDTO dto) {
        return ConstraintReaderHelpers.parseDate((String) (value).get("date"), dto);
    }
}
