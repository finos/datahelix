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

package com.scottlogic.datahelix.generator.profile.services;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.profile.InSetRecord;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.datahelix.generator.profile.reader.CsvInputStreamReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.scottlogic.datahelix.generator.core.profile.constraints.atomic.NameConstraintTypes.*;

public class NameRetrievalService
{
    private final CsvInputStreamReaderFactory csvReaderFactory;

    @Inject
    public NameRetrievalService(CsvInputStreamReaderFactory csvReaderFactory) {
        this.csvReaderFactory = csvReaderFactory;
    }

    public List<InSetRecord> loadNamesFromFile(NameConstraintTypes configuration) {
        if (configuration == FULL) {
            return combineFirstWithLastNames(
                    generateNamesFromSingleFile(FIRST.getFilePath()),
                    generateNamesFromSingleFile(LAST.getFilePath()));
        } else {
            return generateNamesFromSingleFile(configuration.getFilePath());
        }
    }

    private List<InSetRecord> generateNamesFromSingleFile(String source) {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(source))
        {
            return csvReaderFactory.getReaderForStream(stream, source).retrieveInSetElements();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static List<InSetRecord> combineFirstWithLastNames(List<InSetRecord> firstNames,
                                                               List<InSetRecord> lastNames) {
        return firstNames.stream()
            .flatMap(
                first -> lastNames.stream()
                    .map(last -> mergeFrequencies(first, last)))
            .distinct()
            .collect(Collectors.toList());
    }

    private static InSetRecord mergeFrequencies(InSetRecord first,
                                                InSetRecord last) {
        String name = String.format("%s %s", first.getElement(), last.getElement());
        double frequency = first.getWeightValueOrDefault() + last.getWeightValueOrDefault();
        return new InSetRecord(name, frequency);
    }
}
