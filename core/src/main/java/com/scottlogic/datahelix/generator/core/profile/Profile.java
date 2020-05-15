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

package com.scottlogic.datahelix.generator.core.profile;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.profile.ProfileFields;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;

import java.util.Collection;
import java.util.List;

public class Profile {
    private final Fields fields;
    private final Collection<Constraint> constraints;
    private final String description;
    private final Collection<Relationship> relationships;

    public Profile(List<Field> fields, Collection<Constraint> constraints, Collection<Relationship> relationships) {
        this(null, new ProfileFields(fields), constraints, relationships);
    }

    public Profile(List<Field> fields, Collection<Constraint> constraints, Collection<Relationship> relationships, String description) {
        this(description, new ProfileFields(fields), constraints, relationships);
    }

    public Profile(Fields fields, Collection<Constraint> constraints, Collection<Relationship> relationships) {
        this(null, fields, constraints, relationships);
    }

    public Profile(String description, Fields fields, Collection<Constraint> constraints, Collection<Relationship> relationships) {
        this.fields = fields;
        this.constraints = constraints;
        this.description = description;
        this.relationships = relationships;
    }

    public Fields getFields() {
        return fields;
    }

    public Collection<Constraint> getConstraints() {
        return constraints;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Relationship> getRelationships() {
        return relationships;
    }
}
