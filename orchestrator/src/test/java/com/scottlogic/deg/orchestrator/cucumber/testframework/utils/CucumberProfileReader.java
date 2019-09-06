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

import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.profile.reader.*;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CucumberProfileReader implements ProfileReader {

    private final CucumberTestState state;

    private final AtomicConstraintTypeReaderMap constraintReaderMap;

    @Inject
    public CucumberProfileReader(CucumberTestState state, AtomicConstraintTypeReaderMap constraintReaderMap) {
        this.state = state;
        this.constraintReaderMap = constraintReaderMap;
    }

    @Override
    public Profile read() {
        return this.getProfile();
    }

    private Profile getProfile() {
        try {
            MainConstraintReader constraintReader = new MainConstraintReader(constraintReaderMap);
            ProfileFields profileFields = new ProfileFields(state.profileFields);
            AtomicBoolean exceptionInMapping = new AtomicBoolean();

            List<Constraint> mappedConstraints = state.constraints.stream().map(dto -> {
                try {
                    return constraintReader.apply(dto, profileFields);
                } catch (InvalidProfileException e) {
                    state.addException(e);
                    exceptionInMapping.set(true);
                    return null;
                }
            }).filter(x->!(x instanceof RemoveFromTree))
                .collect(Collectors.toList());

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
}
