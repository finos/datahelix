package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines an output target which stores the output data into the test state.
 */
public class InMemoryOutputTarget implements SingleDatasetOutputTarget {
    private final CucumberTestState testState;

    InMemoryOutputTarget(CucumberTestState testState) {
        this.testState = testState;
    }

    @Override
    public DataSetWriter openWriter(ProfileFields fields) {
        return new DummyWriter(fields, testState.generatedObjects);
    }

    private class DummyWriter implements DataSetWriter {
        private final ProfileFields fields;
        private final List<List<Object>> listToAppendTo;

        DummyWriter(ProfileFields fields, List<List<Object>> listToAppendTo) {
            this.fields = fields;
            this.listToAppendTo = listToAppendTo;
        }

        @Override
        public void writeRow(GeneratedObject row) {
            if (row == null) {
                throw new IllegalStateException("GeneratedObject is null");
            }

            List<Object> values = fields.stream()
                .map(row::getValueAndFormat)
                .map(DataBagValue::getFormattedValue)
                .collect(Collectors.toList());

            listToAppendTo.add(values);
        }

        @Override
        public void close() {}
    }
}
