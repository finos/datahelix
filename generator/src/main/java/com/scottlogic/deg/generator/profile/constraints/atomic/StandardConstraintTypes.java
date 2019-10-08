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

package com.scottlogic.deg.generator.profile.constraints.atomic;

public enum StandardConstraintTypes{
    SEDOL("[B-DF-HJ-NP-TV-Z0-9]{6}[0-9]"),
    ISIN("[A-Z]{2}[A-Z0-9]{10}"),
    CUSIP("[0-9]{3}[0-9A-Z]{5}[0-9]"),
    RIC("[A-Z]{1,4}\\.[A-Z]{1,2}");

    private final String regex;

    StandardConstraintTypes(String regex) {

        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
