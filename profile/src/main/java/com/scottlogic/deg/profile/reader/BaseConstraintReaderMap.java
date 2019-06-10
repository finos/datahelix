package com.scottlogic.deg.profile.reader;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BaseConstraintReaderMap implements ConstraintReaderMap {
    private final Map<String, Map<String, ConstraintReader>> map = new HashMap<>();

    public BaseConstraintReaderMap(Stream<ConstraintReaderMapEntryProvider> providersToLoad) {
        providersToLoad.forEach(p -> add(p.getConstraintReaderMapEntries()));
    }

    @Override
    public ConstraintReader getReader(String typeCode, String valueCode) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, ConstraintReader> innerMap = map.get(typeCode);
        if (innerMap == null || innerMap.isEmpty()) {
            throw new InvalidProfileException("Profile is invalid: no constraints known for " + typeCode);
        }

        // handle "is": "X", "value": "Y" cases
        if (valueCode != null) {
            ConstraintReader func = innerMap.get(valueCode);
            if (func != null) {
                return func;
            }
            for (Map.Entry<String, ConstraintReader> entry : innerMap.entrySet()) {
                if (Pattern.matches(entry.getKey(), valueCode)) {
                    return entry.getValue();
                }
            }
            // Throw exception if there are entries for X but no match to Y
            throw new InvalidProfileException(String.format(
                "Profile is invalid: no constraints known for \"is\": \"%s\", \"value\": \"%s\"",
                typeCode,
                valueCode
            ));
        }

        // handle bare "is": "X" cases with no "value" part.
        // Look for X/any matches first, and fall back to an arbitrary X match second.
        ConstraintReader func = innerMap.get(".*");
        if (func != null) {
            return func;
        }
        return innerMap.values().iterator().next();
    }

    @Override
    public void add(ConstraintReaderMapEntry entry) {
        if (!map.containsKey(entry.typeCode)) {
            map.putIfAbsent(entry.typeCode, new HashMap<>());
        }
        Map<String, ConstraintReader> innerMap = map.get(entry.typeCode);
        innerMap.put(entry.valueCode, entry.reader);
    }

    @Override
    public void add(Stream<ConstraintReaderMapEntry> entries) {
        entries.forEach(e -> add(e));
    }
}
