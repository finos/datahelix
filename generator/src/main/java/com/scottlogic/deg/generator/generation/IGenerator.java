package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;

public interface IGenerator {
    Iterable<TestCaseDataRow> generateData(
        ProfileFields fields,
        IDataBagSource dataBagSource,
        GenerationConfig generationConfig);
}
