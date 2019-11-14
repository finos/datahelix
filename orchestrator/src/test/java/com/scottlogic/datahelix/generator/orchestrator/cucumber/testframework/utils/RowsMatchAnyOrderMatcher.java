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

package com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils;

import org.hamcrest.Description;

import java.util.List;
import java.util.Map;

public class RowsMatchAnyOrderMatcher extends RowsPresentMatcher {
    public RowsMatchAnyOrderMatcher(List<Map<String, Object>> expectedRows) {
        super(expectedRows);
    }

    @Override
    public boolean matches(Object o) {
        List<Map<String, Object>> actualRows = (List<Map<String, Object>>) o;

        if (expectedRows.size() != actualRows.size())
            return false;

        return super.matches(actualRows);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        List<Map<String, Object>> actualRows = (List<Map<String, Object>>) item;

        super.describeMismatch(item, description);

        description.appendText(String.format("\n  counts: expected %d, but got %d", expectedRows.size(), actualRows.size()));
    }
}
