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

package com.scottlogic.deg.common.profile.constraints;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
/*
 * The following JsonTypeInfo is needed for the utility program GenTreeJson.java
 * (invoked via the mode of `genTreeJson`), which produces a JSON for the decision
 * tree in memory.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
        )
@JsonSubTypes({
    @JsonSubTypes.Type(value = IsInSetConstraint.class, name = "IsInSetConstraint"),
    @JsonSubTypes.Type(value = IsStringShorterThanConstraint.class, name = "IsStringShorterThanConstraint"),
    @JsonSubTypes.Type(value = NotConstraint.class, name = "NotConstraint"),
    @JsonSubTypes.Type(value = IsNullConstraint.class, name = "IsNullConstraint"),
    @JsonSubTypes.Type(value = IsLessThanConstantConstraint.class, name = "IsLessThanConstantConstraint")
})
public interface Constraint
{
    Constraint negate();
}

