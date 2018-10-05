package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.DataBagValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** A set of values representing one complete, discrete output (eg, this could be used to make a full CSV row) */
public class GeneratedObject {
    public final List<DataBagValue> values;

    public GeneratedObject(List<DataBagValue> values) {
        this.values = Collections.unmodifiableList(values);
    }
    public GeneratedObject(DataBagValue... values) {
        this(Arrays.asList(values));
    }
}
