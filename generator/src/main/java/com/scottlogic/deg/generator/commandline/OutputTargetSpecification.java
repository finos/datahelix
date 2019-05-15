package com.scottlogic.deg.generator.commandline;

import com.scottlogic.deg.generator.outputs.targets.MultiDatasetOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;

/** A representation of the destination the user specified on the command line (even if nothing was explicitly specified) */
public interface OutputTargetSpecification {
    SingleDatasetOutputTarget asFilePath();
    MultiDatasetOutputTarget asViolationDirectory();
}
