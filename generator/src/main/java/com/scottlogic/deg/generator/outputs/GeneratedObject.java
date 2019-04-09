package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.Collections;
import java.util.List;

/** A set of values representing one complete, discrete output (eg, this could be used to make a full CSV row) */
public class GeneratedObject {
    public final List<DataBagValue> values;
    public RowSource source;

    public GeneratedObject(List<DataBagValue> values, RowSource source) {
        this.values = Collections.unmodifiableList(values);
        this.source = source;
    }
}
