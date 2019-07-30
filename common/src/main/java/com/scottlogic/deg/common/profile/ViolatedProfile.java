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

package com.scottlogic.deg.common.profile;

import java.util.Collection;

public class ViolatedProfile extends Profile {
    /**
     * Original (un-violated) form of the rule that has been violated in this profile.
     */
    public final Rule violatedRule;

    /**
     * Constructs a new violated profile using the base profile constructor.
     * @param schemaVersion Schema version of profile.
     * @param violatedRule Un-violated form of the rule that has been violated on this profile.
     * @param fields Fields relating to this profile.
     * @param rules Collection of rules on this profile, including the violated form of the one rule which has been
     *              violated.
     * @param description Description of profile.
     */
    public ViolatedProfile(String schemaVersion, Rule violatedRule, ProfileFields fields, Collection<Rule> rules, String description){
        super(schemaVersion, fields, rules, description);
        this.violatedRule = violatedRule;
    }

}
