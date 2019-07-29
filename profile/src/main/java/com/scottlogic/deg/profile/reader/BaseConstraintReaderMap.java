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

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BaseConstraintReaderMap implements ConstraintReaderMap {
    private final Map<String, Map<String, ConstraintReader>> operatorAndValueToReadermap =
        new HashMap<>();

    public BaseConstraintReaderMap(Stream<ConstraintReaderMapEntrySource> providersToLoad) {
        providersToLoad
            .flatMap(ConstraintReaderMapEntrySource::getConstraintReaderMapEntries)
            .forEach(this::add);
    }

    public void add(ConstraintReaderMapEntry entry) {
        if (!operatorAndValueToReadermap.containsKey(entry.getOperatorCode())) {
            operatorAndValueToReadermap.putIfAbsent(entry.getOperatorCode(), new HashMap<>());
        }
        Map<String, ConstraintReader> valueToReaderMap =
            operatorAndValueToReadermap.get(entry.getOperatorCode());
        valueToReaderMap.put(entry.getValueCode(), entry.getReader());
    }

    @Override
    public ConstraintReader getReader(String operatorCode, String valueCode) {
        if (operatorAndValueToReadermap.isEmpty()) {
            return null;
        }
        Map<String, ConstraintReader> valueToReaderMap = operatorAndValueToReadermap.get(operatorCode);
        if (valueToReaderMap == null || valueToReaderMap.isEmpty()) {
            throw new InvalidProfileException(
                "Profile is invalid: no constraints known for " +
                    operatorCode
            );
        }

        // handle "is": "X", "value": "Y" cases
        if (valueCode != null) {
            ConstraintReader func = valueToReaderMap.get(valueCode);
            if (func != null) {
                return func;
            }
            for (Map.Entry<String, ConstraintReader> entry : valueToReaderMap.entrySet()) {
                if (Pattern.matches(entry.getKey(), valueCode)) {
                    return entry.getValue();
                }
            }
            // Throw exception if there are entries for X but no match to Y
            throw new InvalidProfileException(String.format(
                "Profile is invalid: no constraints known for \"is\": \"%s\", \"value\": \"%s\"",
                operatorCode,
                valueCode
            ));
        }

        // handle bare "is": "X" cases with no "value" part.
        // Look for X/any matches first, and fall back to an arbitrary X match second.
        ConstraintReader func = valueToReaderMap.get(".*");
        if (func != null) {
            return func;
        }
        return valueToReaderMap.values().iterator().next();
    }
}
