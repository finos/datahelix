package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

public class AtomicConstraintDetailReader {
    public static Object getValue(ConstraintDTO dto){
        if (dto.values != null){
            return dto.values;
        }

        if (dto.file != null){
            return getFileValues(dto.file);
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

    private static Object getFileValues(String file) {
        throw new NotImplementedException();
    }

}
