package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.DataBagValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestCaseDataRow {
    public final List<DataBagValue> values;

    public TestCaseDataRow(List<DataBagValue> values) {
        this.values = Collections.unmodifiableList(values);
    }
    public TestCaseDataRow(DataBagValue... values) {
        this(Arrays.asList(values));
    }
}
