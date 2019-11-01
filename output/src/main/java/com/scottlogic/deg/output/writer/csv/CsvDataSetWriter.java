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

package com.scottlogic.deg.output.writer.csv;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

class CsvDataSetWriter implements DataSetWriter {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final CSVFormat writerFormat = CSVFormat.RFC4180;

    private final CSVPrinter csvPrinter;
    private final Fields fieldOrder;

    private CsvDataSetWriter(CSVPrinter csvPrinter, Fields fieldOrder) {
        this.csvPrinter = csvPrinter;
        this.fieldOrder = fieldOrder;
    }

    static DataSetWriter open(OutputStream stream, Fields fields) throws IOException {
        final Appendable outputStreamAsAppendable = new OutputStreamWriter(stream, StandardCharsets.UTF_8);

        CSVPrinter csvPrinter = writerFormat
            .withQuoteMode(QuoteMode.MINIMAL)
            .withHeader(fields.getExternalStream()
                .map(f -> f.getName())
                .toArray(String[]::new))
            .print(outputStreamAsAppendable);

        return new CsvDataSetWriter(csvPrinter, fields);
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        csvPrinter.printRecord(fieldOrder.getExternalStream()
                .map(row::getFormattedValue)
                .map(CsvDataSetWriter::applyTypeSpecificFormatting)
                .collect(Collectors.toList()));

        csvPrinter.flush();
    }

    @Override
    public void close() throws IOException {
        csvPrinter.close();
    }

    private static Object applyTypeSpecificFormatting(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }

        if (value instanceof OffsetDateTime) {
            return standardDateFormat.format((OffsetDateTime) value);
        }

        return value;
    }
}
