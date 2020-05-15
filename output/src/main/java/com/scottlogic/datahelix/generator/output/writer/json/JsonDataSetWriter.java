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

package com.scottlogic.datahelix.generator.output.writer.json;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.common.output.RelationalGeneratedObject;
import com.scottlogic.datahelix.generator.common.output.SubGeneratedObject;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.output.writer.DataSetWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class JsonDataSetWriter implements DataSetWriter {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final SequenceWriter writer;
    private final List<Field> fields;

    JsonDataSetWriter(SequenceWriter writer, Fields fields) {
        this.writer = writer;
        this.fields = fields.getExternalStream().collect(Collectors.toList());
    }

    private JsonDataSetWriter(List<Field> fields) {
        this.fields = fields;
        this.writer = null;
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        Map<Field, Object> jsonObject = convertRow(row);

        writer.write(jsonObject);
    }

    private Map<Field, Object> convertRow(GeneratedObject row) {
        Map<Field, Object> jsonObject = new HashMap<>();

        fields
            .forEach(field ->
                jsonObject.put(field, convertValue(row.getFormattedValue(field))));

        if (row instanceof RelationalGeneratedObject) {
            writeRelatedObjects(jsonObject, (RelationalGeneratedObject)row);
        }

        return jsonObject;
    }

    private void writeRelatedObjects(Map<Field, Object> jsonObject, RelationalGeneratedObject relationalGeneratedObject) {
        relationalGeneratedObject.getSubObjects()
            .forEach((key, value) -> writeRelatedObject(jsonObject, key, value));
    }

    private void writeRelatedObject(Map<Field, Object> jsonObject, String key, SubGeneratedObject value) {
        JsonDataSetWriter subWriter = new JsonDataSetWriter(value.getFields());
        Field fieldForRelationship = new Field(key, null, false, null, false, true, null);

        if (value.isArray()) {
            writeRelatedArray(jsonObject, value, subWriter, fieldForRelationship);
        } else {
            writeRelatedObject(jsonObject, value, subWriter, fieldForRelationship);
        }
    }

    private void writeRelatedObject(Map<Field, Object> jsonObject, SubGeneratedObject value, JsonDataSetWriter subWriter, Field fieldForRelationship) {
        GeneratedObject singleSubObject = value.getData().get(0);
        Map<Field, Object> subObject = subWriter.convertRow(singleSubObject);
        jsonObject.put(fieldForRelationship, subObject);
    }

    private void writeRelatedArray(Map<Field, Object> jsonObject, SubGeneratedObject value, JsonDataSetWriter subWriter, Field fieldForRelationship) {
        List<Map<Field, Object>> subObjectsForRelationship = value.getData()
            .stream()
            .map(subWriter::convertRow)
            .collect(Collectors.toList());
        jsonObject.put(fieldForRelationship, subObjectsForRelationship);
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
