package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.profile.reader.AtomicConstraintReaderLookup;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.MainConstraintReader;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CucumberProfileReader implements ProfileReader {

    private final CucumberTestState state;

    private final AtomicConstraintReaderLookup atomicConstraintReaderLookup;

    @Inject
    public CucumberProfileReader(CucumberTestState state, AtomicConstraintReaderLookup atomicConstraintReaderLookup) {
        this.state = state;
        this.atomicConstraintReaderLookup = atomicConstraintReaderLookup;
    }

    @Override
    public Profile read(Path filePath) {
        return this.getProfile();
    }

    private Profile getProfile() {
        try {
            MainConstraintReader constraintReader = new MainConstraintReader(atomicConstraintReaderLookup);
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
                Exception firstException = state.testExceptions.get(0);
                if (firstException instanceof InvalidProfileException){
                    throw (InvalidProfileException)firstException;
                }

                if (firstException instanceof JsonParseException){
                    throw (JsonParseException)firstException;
                }

                throw new RuntimeException(firstException);
            }

            return new Profile(profileFields, Collections.singletonList(new Rule(new RuleInformation(), mappedConstraints)));
        } catch (JsonParseException e) {
            state.addException(e);
            throw e;
        }
    }

    private static Set<RuleInformation> getRules(){
        return Collections.singleton(new RuleInformation());
    }
}
