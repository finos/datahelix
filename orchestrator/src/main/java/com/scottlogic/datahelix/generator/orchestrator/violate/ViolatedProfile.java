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

package com.scottlogic.datahelix.generator.orchestrator.violate;

import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;

import java.util.Collection;

public class ViolatedProfile extends Profile {
    /**
     * Original (un-violated) form of the rule that has been violated in this profile.
     */
    public final Constraint violatedConstraint;

    public ViolatedProfile(Constraint violatedConstraint, Fields fields, Collection<Constraint> constraints, String description){
        super(description, fields, constraints);
        this.violatedConstraint = violatedConstraint;
    }

}
