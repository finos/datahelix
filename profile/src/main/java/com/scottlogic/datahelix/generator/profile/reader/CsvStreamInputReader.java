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

package com.scottlogic.datahelix.generator.profile.reader;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.profile.InSetRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class CsvStreamInputReader implements CsvInputReader {
    private final InputStream stream;
    private final String file;

    public CsvStreamInputReader(InputStream stream, String file) {
        this.stream = stream;
        this.file = file;
    }

    public List<InSetRecord> retrieveInSetElements() {
        List<CSVRecord> records = parse(stream);
        return records.stream()
            .map(this::createInSetRecord)
            .collect(Collectors.toList());
    }

    public List<String> retrieveLinesForColumn(String key) {
        List<CSVRecord> records = parse(stream);

        int index = getIndexForKey(records.get(0), key);

        //Remove the header
        records.remove(0);

        return records.stream()
            .map(record -> record.get(index))
            .collect(Collectors.toList());
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

    private InSetRecord createInSetRecord(CSVRecord record) {
        final String value = record.get(0);
        try {
            return record.size() == 1
                ? new InSetRecord(value)
                : new InSetRecord(value, Double.parseDouble(record.get(1)));
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                "Weighting '" + record.get(1) + "' is not a valid number\n" +
                "CSV lines containing 2 columns must hold a weighting (double) in the second column, e.g. <value>,0.5\n" +
                "Value: '" + value + "', File: '" + this.file + "', Line " + record.getRecordNumber(), e);
        }
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
