package com.scottlogic.deg.generator.outputs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class TestCaseDataRow {
    public final Collection<Object> values;

    public TestCaseDataRow(Collection<Object> values) {
        this.values = Collections.unmodifiableCollection(values);
    }
    public TestCaseDataRow(Object... values) {
        this(Arrays.asList(values));
    }
}
