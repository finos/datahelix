package com.scottlogic.deg.generator.outputs.targets;

public interface MultiDatasetOutputTarget {
    SingleDatasetOutputTarget getSubTarget(String name);
}
