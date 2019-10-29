package com.scottlogic.deg.common.util;

import com.scottlogic.deg.common.profile.DateTimeGranularity;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.Granularity;
import com.scottlogic.deg.common.profile.NumericGranularity;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;

public class GranularityUtils {
    public static Granularity readGranularity(FieldType type, String offsetUnit) {
        switch (type) {
            case NUMERIC:
                return offsetUnit != null
                    ? NumericGranularity.create(offsetUnit)
                    : NumericDefaults.get().granularity();
            case DATETIME:
                return offsetUnit != null
                    ? DateTimeGranularity.create(offsetUnit)
                    : DateTimeDefaults.get().granularity();

            default:
                return null;
        }
    }
}
