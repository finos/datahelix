package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.schemas.v3.RuleDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Responsible for controlling the generation of data.
 */
public class TestHelper {

    private DegTestState state;
    private List <List<Object>> generatedData;

    public TestHelper(DegTestState state){
        this.state = state;
    }

    public Object generate(){
        try {
             InMemoryOutputTarget outputTarget = new InMemoryOutputTarget();

            Injector injector = Guice.createInjector();
            new GenerateExecute(
                new GenerationConfig(this.state),
                new CucumberProfileReader(this.state, getRules()),
                injector.getInstance(GenerationEngine.class),
                this.state,
                outputTarget
            ).run();

            return outputTarget.getRows();
        } catch (Exception e) {
            this.state.addException(e);
            return null;
        }
    }

    public List <List<Object>> generateAndGetData() {
        if (state.generationStrategy == null) {
            throw new RuntimeException("Gherkin error: Please specify the data strategy");
        }
        
        if (state.combinationStrategy == null) {
            state.combinationStrategy = GenerationConfig.CombinationStrategyType.PINNING;
        }

        if (this.state.generationMode == null) {
            this.state.generationMode = GenerationConfig.GenerationMode.VALIDATING;
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
                    return constraintReader.apply(dto, profileFields, getRules());
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

    private static Set<RuleInformation> getRules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "getRules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
