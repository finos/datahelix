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

package com.scottlogic.deg.profile.reader;

public class ConstraintReaderMapEntry {
    private String operatorCode;
    private String valueCode;
    private ConstraintReader reader;

    public ConstraintReaderMapEntry(String operatorCode, String valueCode, ConstraintReader reader) {
        this.operatorCode = operatorCode;
        this.valueCode = valueCode;
        this.reader = reader;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public String getValueCode() {
        return valueCode;
    }

    public ConstraintReader getReader() {
        return reader;
    }
}
