package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.util.stream.Stream;

public class InMemoryOutputTarget implements OutputTarget {

    private Stream<GeneratedObject> generatedObjects;

    @Override
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) {
        this.generatedObjects = generatedObjects;
    }

    //TODO: Don't Want This
    @Override
    public OutputTarget withFilename(String filename) {
        return this;
    }

    public Object getRows() {
        return this.generatedObjects;
    }
}
