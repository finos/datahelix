package com.scottlogic.deg.profile.reader;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BaseConstraintReaderMap implements ConstraintReaderMap {
    private final Map<String, Map<String, ConstraintReader>> operatorAndValueToReadermap =
        new HashMap<>();

    public BaseConstraintReaderMap(Stream<ConstraintReaderMapEntrySource> providersToLoad) {
        providersToLoad.forEach(p -> add(p.getConstraintReaderMapEntries()));
    }

    @Override
    public ConstraintReader getReader(String operatorCode, String valueCode) {
        if (operatorAndValueToReadermap == null || operatorAndValueToReadermap.isEmpty()) {
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

    @Override
    public void add(ConstraintReaderMapEntry entry) {
        if (!operatorAndValueToReadermap.containsKey(entry.operatorCode)) {
            operatorAndValueToReadermap.putIfAbsent(entry.operatorCode, new HashMap<>());
        }
        Map<String, ConstraintReader> valueToReaderMap =
            operatorAndValueToReadermap.get(entry.operatorCode);
        valueToReaderMap.put(entry.valueCode, entry.reader);
    }

    @Override
    public void add(Stream<ConstraintReaderMapEntry> entries) {
        entries.forEach(e -> add(e));
    }
}
