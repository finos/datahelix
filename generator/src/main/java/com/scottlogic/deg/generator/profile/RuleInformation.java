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

package com.scottlogic.deg.generator.profile;

import java.util.Objects;

public class RuleInformation {
    private final String description;

    public RuleInformation(){
        this(null);
    }

    public RuleInformation(String rule) {
        this.description = rule != null ? rule : "Unnamed rule";
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleInformation that = (RuleInformation) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
