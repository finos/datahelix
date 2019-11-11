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

package com.scottlogic.deg.output.writer.json;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.output.writer.DataSetWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class JsonDataSetWriter implements DataSetWriter {

    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final SequenceWriter writer;
    private final Fields fields;

    JsonDataSetWriter(SequenceWriter writer, Fields fields) {
        this.writer = writer;
        this.fields = fields;
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        Map<Field, Object> jsonObject = new HashMap<>();

        fields.getExternalStream()
            .forEach(field -> jsonObject
            .put(field , convertValue(row.getFormattedValue(field))));

        writer.write(jsonObject);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }


    private static Object convertValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return value;
        } else if (value instanceof Integer) {
            return value;
        } else if (value instanceof String) {
            return value;
        } else if (value instanceof OffsetDateTime) {
            return standardDateFormat.format((OffsetDateTime) value);
        } else {
            return value.toString();
        }
    }
}
