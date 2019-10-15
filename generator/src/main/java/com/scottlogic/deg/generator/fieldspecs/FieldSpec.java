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

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullAppendingValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;

public abstract class FieldSpec {

    public abstract boolean permits(Object value);
    public abstract FieldValueSource getFieldValueSource();
    public abstract FieldSpec withNotNull();

    protected final boolean nullable;

    public FieldSpec(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isNullable(){
        return nullable;
    }

    protected FieldValueSource appendNullSource(FieldValueSource source){
        if (!nullable){
            return source;
        }
        return new NullAppendingValueSource(source);
    }

}
