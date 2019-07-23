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

package com.scottlogic.deg.output.writer.sql;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.output.writer.DataSetWriter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

class SqlDataSetWriter implements DataSetWriter {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final Writer writer;
    private final ProfileFields fieldOrder;
    private final String tableName;

    SqlDataSetWriter(Writer writer, ProfileFields fieldOrder, String tableName) {
        this.writer = writer;
        this.fieldOrder = fieldOrder;
        this.tableName = tableName;
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        // eg insert into dbo.Currencies (currency, from_date, to_date) values
        //    ( 'YYY', '1762-02-16T00:00:00Z', '1980-02-16T00:00:00Z');

        Optional<String> columnNamesString = reduceStringStreamToParameterString(
            fieldOrder.stream()
                .map(Field::name)
        );
        Optional<String> columnValuesString = reduceStringStreamToParameterString(
            fieldOrder.stream()
                .map(row::getFormattedValue)
                .map(SqlDataSetWriter::fieldValueToString)
        );

        if (!columnNamesString.isPresent() || !columnValuesString.isPresent()) {
            return;
        }

        // TODO AF: this is prone to SQL injection attacks so should do some post processing on the names
        // and values to make sure they are valid and non-malicious
        writer
            .append("INSERT INTO ")
            .append(tableName).append(" (")
            .append(columnNamesString.get())
            .append(") VALUES (")
            .append(columnValuesString.get())
            .append(");\n")
            .flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    private static String fieldValueToString(Object value) {
        if (value == null) {
            return "NULL";
        }
        else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        else if (value instanceof OffsetDateTime) {
            return "'" + standardDateFormat.format((OffsetDateTime) value) + "'";
        }
        else if (value instanceof Number) {
            return value.toString();
        }
        else {
            return "'" + value.toString() + "'";
        }
    }

    private static Optional<String> reduceStringStreamToParameterString(Stream<String> stream) {
        return stream.reduce((t, u) -> t + ", " + u);
    }
}
