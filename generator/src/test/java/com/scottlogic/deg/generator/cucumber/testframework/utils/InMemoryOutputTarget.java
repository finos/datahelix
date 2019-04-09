package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
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
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IllegalStateException {
        this.testState.generatedObjects = getRows(generatedObjects, profileFields);
    }

    private List<List<Object>> getRows(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IllegalStateException {
        return generatedObjects
            .collect(Collectors.toList())
            .stream()
            .map(genObj -> {

                if (genObj == null) {
                    throw new IllegalStateException("GeneratedObject is null");
                }

                return genObj.getOrderedValues(profileFields)
                    .stream()
                    .map(DataBagValue::getFormattedValue)
                    .collect(Collectors.toList());
            }).collect(Collectors.toList());
    }
}
