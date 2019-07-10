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

package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.*;

public final class NameRetriever {

    private NameRetriever() {
        throw new UnsupportedOperationException("No static class instantiation");
    }

    public static Set<Object> loadNamesFromFile(NameConstraintTypes configuration) {
        if (configuration == FULL) {
            return new HashSet<>(combineFirstWithLastNames(
                generateNamesFromSingleFile(FIRST.getFilePath()),
                generateNamesFromSingleFile(LAST.getFilePath())));
        } else {
            return new HashSet<>(generateNamesFromSingleFile(configuration.getFilePath()));
        }
    }

    private static Set<String> generateNamesFromSingleFile(String source) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(source);
        Set<String> result = CsvInputStreamReader.retrieveLines(stream);
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private static Set<String> combineFirstWithLastNames(Set<String> firstNames, Set<String> lastNames) {
        return firstNames.stream()
            .flatMap(
                first -> lastNames.stream()
                    .map(last -> String.format("%s %s", first, last))
            ).collect(Collectors.toSet());
    }

}
