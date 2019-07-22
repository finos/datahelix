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

import com.google.common.collect.Lists;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(JUnitPlatform.class)
class SqlDataSetWriterTests {

    private SqlDataSetWriter sqlDataSetWriter;
    private Writer stringWriter;
    private Writer mockWriter;
    private ProfileFields fieldOrder;
    private String tableName;
    private Field field1String;
    private Field field2Double;
    private Field field3Int;
    private Field field4Date;
    private String formattedField1;
    private Double formattedField2;
    private Integer formattedField3;
    private OffsetDateTime formattedField4;
    private GeneratedObject generatedObject;

    @BeforeEach
    public void setUp() {
        mockWriter = mock(Writer.class);
        stringWriter = new StringWriter();
        tableName = "MY TABLE";
        field1String = new Field("field1String");
        field2Double = new Field("field2Double");
        field3Int = new Field("field3Int");
        field4Date = new Field("field4Date");
        formattedField1 = "F1";
        formattedField2 = 200.123;
        formattedField3 = 300;
        formattedField4 = OffsetDateTime.MIN;
        createFieldOrderAndGeneratedObject();
    }

    private void createFieldOrderAndGeneratedObject() {
        fieldOrder = new ProfileFields(Lists.newArrayList(field1String, field2Double, field3Int, field4Date));
        generatedObject = field -> {
            if (field.equals(field1String)) {
                return formattedField1;
            }
            else if (field.equals(field2Double)) {
                return formattedField2;
            }
            else if (field.equals(field3Int)) {
                return formattedField3;
            }
            else if (field.equals(field4Date)) {
                return formattedField4;
            }
            else {
               throw new IllegalArgumentException("unexpected field: " + field.name);
            }
        };

    }

    @Test
    void writeRow_oneOfEachType() throws Exception {
        String res = callWriteRow();
        String expectedDateStringOutput = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(formattedField4);
        assertEquals("INSERT INTO " + tableName
            + " (field1String, field2Double, field3Int, field4Date) VALUES ('F1', 200.123, 300, '"
            + expectedDateStringOutput + "');\n",
            res);
    }

    @Test
    void writeRow_nullForEachType () throws Exception {
        formattedField1 = null;
        formattedField2 = null;
        formattedField3 = null;
        formattedField4 = null;
        createFieldOrderAndGeneratedObject();

        String res = callWriteRow();
        assertEquals("INSERT INTO " + tableName
            + " (field1String, field2Double, field3Int, field4Date) VALUES (NULL, NULL, NULL, NULL);\n", res);
    }

    @Test
    void writeRow_first2Nulls() throws Exception {
        formattedField1 = null;
        formattedField2 = null;
        createFieldOrderAndGeneratedObject();

        String res = callWriteRow();
        String expectedDateStringOutput = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(formattedField4);
        assertEquals("INSERT INTO " + tableName
            + " (field1String, field2Double, field3Int, field4Date) VALUES (NULL, NULL, 300, '"
            + expectedDateStringOutput + "');\n",
            res);
    }

    @Test
    void writeRow_last2Nulls() throws Exception {
        formattedField3 = null;
        formattedField4 = null;
        createFieldOrderAndGeneratedObject();

        String res = callWriteRow();
        assertEquals("INSERT INTO " + tableName
            + " (field1String, field2Double, field3Int, field4Date) VALUES ('F1', 200.123, NULL, NULL);\n",
            res);
    }

    @Test
    void close() throws Exception {
        sqlDataSetWriter = new SqlDataSetWriter(mockWriter, fieldOrder, tableName);
        sqlDataSetWriter.close();
        Mockito.verify(mockWriter, times(1)).close();
    }

    private String callWriteRow() throws IOException {
        sqlDataSetWriter = new SqlDataSetWriter(stringWriter, fieldOrder, tableName);
        sqlDataSetWriter.writeRow(generatedObject);
        return stringWriter.toString();
    }

}
