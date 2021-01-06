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
package com.scottlogic.datahelix.generator.profile.reader.file;

import com.scottlogic.datahelix.generator.common.profile.InSetRecord;
import com.scottlogic.datahelix.generator.profile.reader.CsvInputReader;
import com.scottlogic.datahelix.generator.profile.reader.CsvStreamInputReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvStreamInputReaderTest {
    @Test
    public void testReadingLinesFromNames() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("names/firstname.csv");
        final CsvInputReader reader = new CsvStreamInputReader(is, "names/firstname.csv");

        final List<InSetRecord> nameElements = reader.retrieveInSetElements();
        final Set<Object> names = nameElements.stream()
            .map(InSetRecord::getElement)
            .collect(Collectors.toSet());

        final Set<String> sampleNames = Stream.of("Rory", "Kyle", "Grace").collect(Collectors.toSet());

        assertTrue(names.containsAll(sampleNames));
        assertTrue(nameElements.stream().allMatch(InSetRecord::hasWeightPresent));
    }

    @Test
    public void testReadingLinesFromFileWithoutFrequencies() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("csv/without-frequencies.csv");
        final CsvInputReader reader = new CsvStreamInputReader(is, "csv/without-frequencies.csv");

        final List<InSetRecord> set = reader.retrieveInSetElements();

        assertTrue(checkAllWeightsAreEquals(set));
        assertTrue(set.stream().noneMatch(InSetRecord::hasWeightPresent));
    }

    private <T> boolean checkAllWeightsAreEquals(List<InSetRecord> set) {
        return set.stream()
            .map(InSetRecord::getWeightValueOrDefault)
            .distinct()
            .limit(2).count() <= 1;
    }

}