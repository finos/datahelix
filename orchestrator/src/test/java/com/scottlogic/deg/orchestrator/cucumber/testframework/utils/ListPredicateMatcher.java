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

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ListPredicateMatcher extends BaseMatcher<List<Object>> {
    private final Function<Object, Boolean> predicate;
    private final HashMap<Integer, Object> rowsThatDoNotMatch = new HashMap<>();
    private Integer checkedRows;

    public ListPredicateMatcher(Function<Object, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description
            .appendText(String.format("%d of %d rows do not match\n", rowsThatDoNotMatch.size(), checkedRows));

        rowsThatDoNotMatch
            .entrySet()
            .forEach(entry -> description.appendText(String.format("Row %d: (%s) does not match predicate\n", entry.getKey(), entry.getValue())));
    }

    @Override
    public boolean matches(Object o) {
        List<Object> values = (List<Object>) o;
        checkedRows = values.size();

        for (int index = 0; index < values.size(); index++) {
            Object value = values.get(index);

            if (!predicate.apply(value)) {
                rowsThatDoNotMatch.put(index, value);
            }
        }

        return rowsThatDoNotMatch.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("All values match predicate");
    }
}
