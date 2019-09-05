package com.scottlogic.deg.profile.reader.atomic;

import com.google.inject.Inject;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;

import java.math.BigDecimal;
import java.util.Map;

public class AtomicConstraintDetailReader {

    private final FromFileReader fromFileReader;

    @Inject
    public AtomicConstraintDetailReader(FromFileReader fromFileReader) {
        this.fromFileReader = fromFileReader;
    }

    public Object getValue(ConstraintDTO dto){
        if (dto.values != null){
            return dto.values;
        }

        if (dto.file != null){
            return fromFileReader.setFromFile(dto.file);
        }

        if (dto.value instanceof Map){
            return ConstraintReaderHelpers.parseDate((String) ((Map) dto.value).get("date"), dto);
        }

        BigDecimal bigDecimal = NumberUtils.coerceToBigDecimal(dto.value);
        if (bigDecimal != null){
            return bigDecimal;
        }

        return dto.value;
    }
}
