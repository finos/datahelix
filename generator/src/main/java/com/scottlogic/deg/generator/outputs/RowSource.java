package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.List;
import java.util.Set;

public class RowSource {
    public List<CellSource> columns;

    public RowSource(List<CellSource> columns) {
        this.columns = columns;
    }
}

