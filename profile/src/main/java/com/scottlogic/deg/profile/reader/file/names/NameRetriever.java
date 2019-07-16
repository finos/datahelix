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
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.constraints.atomic.NameConstraintTypes.*;

public final class NameRetriever {

    private NameRetriever() {
        throw new UnsupportedOperationException("No static class instantiation");
    }

    public static DistributedSet<Object> loadNamesFromFile(NameConstraintTypes configuration) {
        if (configuration == FULL) {
            return downcastToObject(combineFirstWithLastNames(
                generateNamesFromSingleFile(FIRST.getFilePath()),
                generateNamesFromSingleFile(LAST.getFilePath())));
        } else {
            return downcastToObject(generateNamesFromSingleFile(configuration.getFilePath()));
        }
    }

    private static <T> DistributedSet<Object> downcastToObject(DistributedSet<T> higher) {
        return new FrequencyDistributedSet<>(
            higher.distributedSet().stream()
                .map(holder -> new WeightedElement<Object>(holder.element(), holder.weight()))
                .collect(Collectors.toSet()));
    }

    ;

    private static DistributedSet<String> generateNamesFromSingleFile(String source) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(source);
        DistributedSet<String> result = CsvInputStreamReader.retrieveLines(stream);
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private static DistributedSet<String> combineFirstWithLastNames(DistributedSet<String> firstNames,
                                                                    DistributedSet<String> lastNames) {
        return new FrequencyDistributedSet<>(firstNames.distributedSet().stream()
            .flatMap(
                first -> lastNames.distributedSet().stream()
                    .map(last -> mergeFrequencies(first, last))
            ).collect(Collectors.toSet()));
    }

    private static WeightedElement<String> mergeFrequencies(WeightedElement<String> first,
                                                            WeightedElement<String> last) {
        String name = String.format("%s %s", first.element(), last.element());
        float frequency = first.weight() + last.weight();
        return new WeightedElement<>(name, frequency);
    }

}
