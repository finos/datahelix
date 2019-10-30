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

package com.scottlogic.deg.profile.dtos.constraints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scottlogic.deg.profile.serialisation.ConstraintDeserializer;
import com.scottlogic.deg.profile.common.ConstraintType;

@JsonDeserialize(using = ConstraintDeserializer.class)
public abstract class ConstraintDTO {
    private final ConstraintType type;

    ConstraintDTO(ConstraintType type) {
        this.type = type;
    }

    @JsonIgnore
    public ConstraintType getType() {
        return type;
    }
}
