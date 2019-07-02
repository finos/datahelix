/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;

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
                .map(row::getFormattedValue)
                .collect(Collectors.toList());

            listToAppendTo.add(values);
        }

        @Override
        public void close() {}
    }
}
