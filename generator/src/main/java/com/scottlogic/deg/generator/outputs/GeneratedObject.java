package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** A set of values representing one complete, discrete output (eg, this could be used to make a full CSV row) */
public class GeneratedObject extends DataBag {
    public GeneratedObject(Map<Field, DataBagValue> fieldToValue) {
        super(fieldToValue);
    }
    public GeneratedObject(DataBag dataBag) {
        super(dataBag.getFieldToValue());
    }
}
