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

import com.scottlogic.deg.common.profile.fields.Field;
import com.scottlogic.deg.common.profile.fields.Fields;
import com.scottlogic.deg.common.profile.fields.SpecificFieldType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldBuilder {
    public static Fields createValidFields()
    {
        return Fields.create(Stream.of(createField("Field", SpecificFieldType.STRING)).collect(Collectors.toList()));
    }

    public static Field createField(String name) {
        return createField(name, SpecificFieldType.STRING);
    }
    public static Field createInternalField(String name) {
        return createInternalField(name, SpecificFieldType.STRING);
    }
    public static Field createField(String name, SpecificFieldType type) {
        return Field.create(name, type, false, null, false, false);
    }
    public static Field createInternalField(String name, SpecificFieldType type) {
        return Field.create(name, type, false, null, true, false);
    }
}
