package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v0_1.RuleDTO;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CucumberProfileReader implements ProfileReader {

    private final CucumberTestState state;

    @Inject
    public CucumberProfileReader(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public Profile read(Path filePath) {
        return this.getProfile();
    }

    private Profile getProfile(){
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

            return new Profile(profileFields, Collections.singletonList(new Rule(new RuleInformation(new RuleDTO()), mappedConstraints)));
        } catch (Exception e) {
            state.addException(e);
            return null;
        }
    }

    private static Set<RuleInformation> getRules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "getRules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
