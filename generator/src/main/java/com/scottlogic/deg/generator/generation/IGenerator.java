package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.Collection;

public interface IGenerator {
    Collection<TestCaseDataRow> generateData(RowSpec spec);

    Collection<TestCaseDataRow> generateData(RowSpec spec, GenerationStrategy strategy);
}
