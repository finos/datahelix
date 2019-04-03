package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
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
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IllegalStateException, InvalidProfileException {
        this.testState.generatedObjects = getRows(generatedObjects);
    }

    private List<List<Object>> getRows(Stream<GeneratedObject> generatedObjects) throws IllegalStateException, InvalidProfileException {
        try {
            return generatedObjects
                .collect(Collectors.toList())
                .stream()
                .map(genObj -> {

                    if (genObj == null) {
                        throw new IllegalStateException("GeneratedObject is null");
                    }

                    return genObj.values
                        .stream()
                        .map(DataBagValue::getFormattedValue)
                        .collect(Collectors.toList());
                }).collect(Collectors.toList());
        } catch (RuntimeException exc){
            throw (InvalidProfileException)exc.getCause();
        }
    }
}
