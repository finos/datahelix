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

package com.scottlogic.datahelix.generator.common.profile;

public class FieldBuilder {
    public static Field createField(String name) {
        return createField(name, StandardSpecificFieldType.STRING.toSpecificFieldType());
    }
    public static Field createInternalField(String name) {
        return createInternalField(name, StandardSpecificFieldType.STRING.toSpecificFieldType());
    }
    public static Field createField(String name, SpecificFieldType type) {
        return new Field(name, type, false, null, false, false, null);
    }
    public static Field createInternalField(String name, SpecificFieldType type) {
        return new Field(name, type, false, null, true, false, null);
    }
}
