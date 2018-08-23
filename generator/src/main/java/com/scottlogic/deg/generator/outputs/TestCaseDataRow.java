package com.scottlogic.deg.generator.outputs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestCaseDataRow {
    public final List<Object> values;

    public TestCaseDataRow(List<Object> values) {
        this.values = Collections.unmodifiableList(values);
    }
    public TestCaseDataRow(Object... values) {
        this(Arrays.asList(values));
    }
}
