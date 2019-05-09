package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

@FunctionalInterface
public interface RowOutputFormatter<T> {

    T format(GeneratedObject row);
}
