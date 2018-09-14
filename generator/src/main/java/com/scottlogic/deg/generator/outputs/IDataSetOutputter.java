package com.scottlogic.deg.generator.outputs;

import java.io.IOException;

public interface IDataSetOutputter {
    void output(TestCaseGenerationResult dataSets) throws IOException;
}
