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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fields implements Iterable<Field>
{
    private final Map<String, Field> fieldMap;

    private Fields(Map<String, Field> fieldMap)
    {
        this.fieldMap = fieldMap;
    }

    public static Fields create(Collection<Field> fields)
    {
        fieldCollectionMustNotBeNullOrEmpty(fields);
        fieldCollectionMustNotContainDuplicateFieldNames(fields);
        Map<String, Field> fieldMap = fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        return new Fields(fieldMap);
    }

    //region<Validation>

    private static void fieldCollectionMustNotBeNullOrEmpty(Collection<Field> fields)
    {
        if (fields == null)
        {
            throw new ValidationException("Fields - field collection must not be null");
        }
        if (fields.isEmpty())
        {
            throw new ValidationException("Fields - field collection must not be empty");
        }
    }

    private static void fieldCollectionMustNotContainDuplicateFieldNames(Collection<Field> fields)
    {
        final Set<String> fieldNames = new HashSet<>();
        final Set<String> duplicateFieldNames = new HashSet<>();

        fields.forEach(field ->
        {
            if(!fieldNames.add(field.getName()))
            {
                duplicateFieldNames.add(field.getName());
            }
        });

        if(!duplicateFieldNames.isEmpty())
        {
            throw new ValidationException("Fields - field collection must not contain duplicate field names - Duplicates: " + String.join(", ", duplicateFieldNames));
        }
    }

    //endregion

    public Field getByName(String fieldName)
    {
        return fieldMap.getOrDefault(fieldName, null);
    }

    public int size()
    {
        return this.fieldMap.size();
    }

    //region<Overrides>

    @Override
    public Iterator<Field> iterator()
    {
        return asList().iterator();
    }

    public Stream<Field> stream()
    {
        return asList().stream();
    }

    public Stream<Field> getExternalStream()
    {
        return this.stream().filter(f -> !f.isInternal());
    }

    public List<Field> asList()
    {
        return new ArrayList<>(fieldMap.values());
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj != null && obj.getClass() == getClass() && this.fieldMap.equals(((Fields) obj).fieldMap);
    }

    @Override
    public int hashCode()
    {
        return fieldMap.hashCode();
    }

    //endregion
}
