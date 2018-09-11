package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;

public class GranularityRestrictions {
    public int numericScale = 0;

    public void setGranularity(String granularity) {
        BigDecimal numericGranularity = NumberUtils.tryParse(granularity);
        if (numericGranularity != null) {
            numericScale = numericGranularity.scale();
        }
    }
}
