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

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public final class CsvInputStreamReader {

    private CsvInputStreamReader() {
        throw new UnsupportedOperationException("No instantiation of static class");
    }

    public static DistributedList<String> retrieveLines(InputStream stream) {
        List<CSVRecord> records = parse(stream);
        return new DistributedList<>(records.stream()
            .map(CsvInputStreamReader::createWeightedElementFromRecord)
            .collect(Collectors.toList()));
    }

    public static DistributedList<String> retrieveLines(InputStream stream, String key) {
        List<CSVRecord> records = parse(stream);

        int index = getIndexForKey(records.get(0), key);

        //Remove the header
        records.remove(0);

        return new DistributedList<>(records.stream()
            .map(record -> record.get(index))
            .map(record -> createWeightedElement(record, Optional.empty()))
            .collect(Collectors.toList()));
    }

    private static int getIndexForKey(CSVRecord header, String key) {
        int index = 0;
        for (String title : header) {
            if (title.equals(key)) {
                return index;
            }
            index++;
        }
        throw new ValidationException("unable to find data for key " + key);
    }

    private static WeightedElement<String> createWeightedElementFromRecord(CSVRecord record) {
        return createWeightedElement(record.get(0),
            record.size() == 1 ? Optional.empty() : Optional.of(Double.parseDouble(record.get(1))));
    }


    private static WeightedElement<String> createWeightedElement(String element, Optional<Double> weight) {
        return weight.map(integer -> new WeightedElement<>(element, integer))
            .orElseGet(() -> WeightedElement.withDefaultWeight(element));
    }

    private static List<CSVRecord> parse(InputStream stream) {
        try {
            CSVParser parser = CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.DEFAULT);
            return parser.getRecords();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
