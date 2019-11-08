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

package com.scottlogic.deg.profile.dtos.constraints.atomic.numeric;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintType;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintTypeJsonProperty;

@JsonDeserialize(as = ShorterThanConstraintDTO.class)
public class ShorterThanConstraintDTO extends NumericConstraintDTO
{
    @JsonProperty(ConstraintTypeJsonProperty.SHORTER_THAN)
    public int value;

    public ShorterThanConstraintDTO() {
        super(ConstraintType.SHORTER_THAN);
    }

    @Override
    @JsonIgnore
    public Number getNumber()
    {
        return value;
    }
}
