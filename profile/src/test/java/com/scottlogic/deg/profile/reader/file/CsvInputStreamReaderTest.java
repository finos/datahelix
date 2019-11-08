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
package com.scottlogic.deg.profile.reader.file;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.profile.reader.CsvInputStreamReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CsvInputStreamReaderTest {

    @Test
    public void testReadingLinesFromNames() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("names/firstname.csv");

        final DistributedList<String> names = CsvInputStreamReader.retrieveLines(is);

        final Set<String> sampleNames = Stream.of("Rory", "Kyle", "Grace").collect(Collectors.toSet());

        assertTrue(names.list().containsAll(sampleNames));
    }

    @Test
    public void testReadingLinesFromFileWithoutFrequencies() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("csv/without-frequencies.csv");

        final DistributedList<String> set = CsvInputStreamReader.retrieveLines(is);

        assertTrue(checkAllWeightsAreEquals(set));
    }

    private <T> boolean checkAllWeightsAreEquals(DistributedList<T> set) {
        return set.distributedList().stream()
            .map(WeightedElement::weight)
            .distinct()
            .limit(2).count() <= 1;
    }

}