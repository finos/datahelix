package com.scottlogic.deg.generator.cucumber.utils;

import java.util.List;

/**
 * Responsible for controlling the generation of data from the DEG.
 */
public class DegTestHelper {

    private DegTestState state;
    private List <List<Object>> generatedData;

    public DegTestHelper(DegTestState state){
        this.state = state;
    }

    public List <List<Object>> generateAndGetData() {
        if (state.generationStrategy == null) {
            throw new RuntimeException("Gherkin error: Please specify the data strategy");
        }

        if (!this.generatorHasRun()){
            try {
                generatedData = GeneratorTestUtilities.getDEGGeneratedData(
                    state.profileFields,
                    state.constraints,
                    state.generationStrategy,
                    state.walkerType
                );
            } catch (Exception e) {
                state.testExceptions.add(e);
            }
        }

        return generatedData;
    }

    public boolean generatorHasRun(){
        return generatedData != null || this.generatorHasThrownException();
    }

    public boolean generatorHasThrownException() {
        return state.testExceptions.size() > 0;
    }

    public boolean hasDataBeenGenerated() {
        return generatedData != null && generatedData.size() > 0;
    }
}
