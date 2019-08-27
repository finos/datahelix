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

package com.scottlogic.deg.generator.generation.databags;

import java.util.IllegalFormatException;
import java.util.Objects;

public class DataBagValue {
    private final Object value;
    private final String format;

    public DataBagValue(Object value, String format){
        this.value = value;
        this.format = format;
    }

    public DataBagValue(Object value) {
        this(value, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBagValue that = (DataBagValue) o;
        return Objects.equals(value, that.value) &&
            Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, format);
    }

    @Override
    public String toString() {
        return "DataBagValue{" +
            "value=" + value +
            ", format='" + format + '\'' +
            '}';
    }

    public Object getFormattedValue() {
        if (format == null || value == null) {
            return value;
        }

        try {
            return String.format(format, value);
        } catch (IllegalFormatException e){
            return value;
        }
    }

    public Object getUnformattedValue(){
        return value;
    }
}

