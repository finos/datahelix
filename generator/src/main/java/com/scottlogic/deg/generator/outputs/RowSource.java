package com.scottlogic.deg.generator.outputs;

import java.util.List;

public class RowSource {
    public List<CellSource> columns;

    public RowSource(List<CellSource> columns) {
        this.columns = columns;
    }
}

