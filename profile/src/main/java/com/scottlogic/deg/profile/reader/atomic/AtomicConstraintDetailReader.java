package com.scottlogic.deg.profile.reader.atomic;

import com.google.inject.Inject;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;

public class AtomicConstraintDetailReader {

    private final FromFileReader fromFileReader;

    @Inject
    public AtomicConstraintDetailReader(FromFileReader fromFileReader) {
        this.fromFileReader = fromFileReader;
    }

    public Object getValue(ConstraintDTO dto){
        if (dto.values != null){
            return getSet(dto.values);
        }

        if (dto.file != null){
            return fromFileReader.setFromFile(dto.file);
        }

        if (dto.value instanceof Map){
            return getDate(dto);
        }

        return dto.value;
    }

    private FrequencyDistributedSet getSet(Object values) {
        if (!(values instanceof Collection)){
            return null;//TODO lul
        }

        return FrequencyDistributedSet.uniform((Collection)values);
    }

    private OffsetDateTime getDate(ConstraintDTO dto) {
        return ConstraintReaderHelpers.parseDate((String) ((Map) dto.value).get("date"), dto);
    }
}
