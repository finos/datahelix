package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines an output target which stores the output data into the test state.
 */
public class InMemoryOutputTarget implements OutputTarget {

    private final CucumberTestState testState;

    @Inject
    public InMemoryOutputTarget(CucumberTestState testState) {
        this.testState = testState;
    }

    @Override
    public void outputDataset(Stream<Row> rows, ProfileFields profileFields) throws IllegalStateException {
        this.testState.rows = getRows(rows, profileFields);
    }

    private List<List<Object>> getRows(Stream<Row> rows, ProfileFields profileFields) throws IllegalStateException {
        return rows
            .collect(Collectors.toList())
            .stream()
            .map(genObj -> {

                if (genObj == null) {
                    throw new IllegalStateException("Row is null");
                }

                return profileFields.stream()
                    .map(field -> genObj.getFieldToValue().get(field).getFormattedValue())
                    .collect(Collectors.toList());
            }).collect(Collectors.toList());
    }
}
