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

package com.scottlogic.deg.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;

@JsonPropertyOrder({ "field", "is", "value", "file", "key", "if", "then", "else" })
public class ConstraintDTO {
    // the DTO is very permissive, because validation isn't its job.
    // validation rules should be expressed in JSON schemas and DTO -> Model converters

    public static final Object undefined = new UndefinedValue();

    public Object is = undefined;

    /** the ID of the field this constraint constrains, if relevant */
    public String field;

    /** a constant value - eg, used in isEqualTo or isGreaterThan */
    public Object value;

    public String otherField;

    public Integer offset;

    public String offsetUnit;

    /** a set of values - eg, used in isInSet */
    public Collection<Object> values;

    public String file;

    public String key;

    /** a constraint to negate - this property should only appear alone */
    public ConstraintDTO not;

    /** a set of subconstraints - used in or/and */
    public Collection<ConstraintDTO> anyOf;

    /** a set of subconstraints - used in or/and */
    public Collection<ConstraintDTO> allOf;

    /** used in condition - should always co-occur with 'then' */
    @JsonProperty("if")
    public ConstraintDTO if_;

    /** the constraint to apply if 'if_' is true */
    public ConstraintDTO then;

    /** the constraint to apply if 'if_' is false */
    @JsonProperty("else")
    public ConstraintDTO else_;

    @Override
    public String toString() {
        return "ConstraintDTO{" +
            "is=" + is +
            ", field='" + field + '\'' +
            ", value=" + value +
            ", values=" + values +
            ", not=" + not +
            ", anyOf=" + anyOf +
            ", allOf=" + allOf +
            ", if_=" + if_ +
            ", then=" + then +
            ", else_=" + else_ +
            '}';
    }
}
