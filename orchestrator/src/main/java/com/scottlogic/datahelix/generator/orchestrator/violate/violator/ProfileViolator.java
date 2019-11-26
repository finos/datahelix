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

package com.scottlogic.datahelix.generator.orchestrator.violate.violator;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.orchestrator.violate.ViolatedProfile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileViolator {
    private final ConstraintViolator constraintViolator;

    @Inject
    public ProfileViolator(ConstraintViolator constraintViolator) {
        this.constraintViolator = constraintViolator;
    }

    public List<ViolatedProfile> violate(Profile profile)
    {
        return profile.getConstraints().stream()
            .map(c -> violateConstraintOnProfile(profile, c)).collect(Collectors.toList());
    }

    private ViolatedProfile violateConstraintOnProfile(Profile profile, Constraint violatedConstraint) {
        Collection<Constraint> newConstraints = profile.getConstraints().stream()
            .map(c -> c == violatedConstraint ? constraintViolator.violateConstraint(violatedConstraint): c)
            .collect(Collectors.toList());

        return new ViolatedProfile(
            violatedConstraint,
            profile.getFields(),
            newConstraints,
            String.format("%s -- Violating: %s", profile.getDescription(), violatedConstraint.toString()));
    }
}
