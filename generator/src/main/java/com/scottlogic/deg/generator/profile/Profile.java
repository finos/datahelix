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

package com.scottlogic.deg.generator.profile;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;

import java.util.Collection;
import java.util.List;

public class Profile {
    private final Fields fields;
    private final Collection<Rule> rules;
    private final String description;

    public Profile(List<Field> fields, Collection<Rule> rules) {
        this(new Fields(fields), rules, null);
    }

    public Profile(List<Field> fields, Collection<Rule> rules, String description) {
        this(new Fields(fields), rules, description);
    }

    public Profile(Fields fields, Collection<Rule> rules) {
        this(fields, rules, null);
    }

    public Profile(Fields fields, Collection<Rule> rules, String description) {
        this.fields = fields;
        this.rules = rules;
        this.description = description;
    }

    public Fields getFields() {
        return fields;
    }

    public Collection<Rule> getRules() {
        return rules;
    }

    public String getDescription() {
        return description;
    }
}
