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

package com.scottlogic.deg.common.profile.fields;

import com.scottlogic.deg.common.ValidationException;

import java.util.Objects;

public class Field
{
    private final String name;
    private final SpecificFieldType specificType;
    private final boolean unique;
    private final String formatting;
    private final boolean internal;
    private final boolean nullable;

    private Field(String name, SpecificFieldType specificType, boolean unique, String formatting, boolean internal, boolean nullable)
    {
        this.name = name;
        this.specificType = specificType;
        this.unique = unique;
        this.formatting = formatting;
        this.internal = internal;
        this.nullable = nullable;
    }

    public static Field create(String name, SpecificFieldType specificType, boolean unique, String formatting, boolean internal, boolean nullable)
    {
        nameMustNotBeNull(name);
        specificTypeMustNotBeNull(name, specificType);
        return new Field(name, specificType, unique, formatting, internal, nullable);
    }

    //region<Getters>

    public String getName()
    {
        return name;
    }

    public SpecificFieldType getSpecificType()
    {
        return specificType;
    }

    public FieldType getType()
    {
        return specificType.getFieldType();
    }

    public boolean isUnique()
    {
        return unique;
    }

    public String getFormatting()
    {
        return formatting;
    }

    public boolean isInternal()
    {
        return internal;
    }

    public boolean isNullable()
    {
        return nullable;
    }

    //endregion

    //region<Validation>

    private static void nameMustNotBeNull(String fieldName)
    {
        if (fieldName == null)
        {
            throw new ValidationException("Field - name must not be null");
        }
    }

    private static void specificTypeMustNotBeNull(String name, SpecificFieldType specificType)
    {
        if (specificType == null)
        {
            throw new ValidationException("Field - specific type must not be null - field: " + name);
        }
    }

    //endregion

    //region<Overrides>

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name)
            && Objects.equals(unique, field.unique)
            && Objects.equals(specificType, field.specificType)
            && Objects.equals(formatting, field.formatting);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, unique, formatting, specificType);
    }

    //endregion
}