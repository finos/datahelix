package com.scottlogic.deg.generator.outputs.targets;

public interface MultiDatasetOutputTarget extends ValidatableOutput {
    SingleDatasetOutputTarget getSubTarget(String name);
}
