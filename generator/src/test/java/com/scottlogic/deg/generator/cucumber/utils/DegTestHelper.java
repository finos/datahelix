package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
        
        if (state.combinationStrategy == null) {
            state.combinationStrategy = GenerationConfig.CombinationStrategyType.PINNING;
        }

        if (this.generatorHasRun()) {
            return generatedData;
        }

        try {
            MainConstraintReader constraintReader = new MainConstraintReader();
            ProfileFields profileFields = new ProfileFields(state.profileFields);
            AtomicBoolean exceptionInMapping = new AtomicBoolean();

            List<Constraint> mappedConstraints = state.constraints.stream().map(dto -> {
                try {
                    return constraintReader.apply(dto, profileFields, RuleInformation.fromDescription("rule"));
                } catch (InvalidProfileException e) {
                    state.addException(e);
                    exceptionInMapping.set(true);
                    return null;
                }
            }).collect(Collectors.toList());

            if (exceptionInMapping.get()){
                return null;
            }

            return generatedData = GeneratorTestUtilities.getDEGGeneratedData(
                state.profileFields,
                mappedConstraints,
                state.generationStrategy,
                state.walkerType,
                state.combinationStrategy
            );
        } catch (Exception e) {
            state.addException(e);
            return null;
        }
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

    public Collection<Exception> getThrownExceptions(){
        return state.testExceptions;
    }
}
