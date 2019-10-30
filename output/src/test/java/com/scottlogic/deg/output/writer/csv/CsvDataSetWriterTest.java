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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.fail;

@RunWith(MockitoJUnitRunner.class)
public class CsvDataSetWriterTest {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private Field fieldOne = new Field("one", FieldType.STRING,false,null,false, false);
    private Field fieldTwo = new Field("two", FieldType.STRING,false,null,false, false);

    private Fields fields = new Fields(new ArrayList<>(Arrays.asList(
        fieldOne, fieldTwo
    )));

    @Mock
    GeneratedObject row;

    @Test
    public void open_createsCSVWriterThatCorrectlyOutputsCommasAndQuotes() {
        DataSetWriter dataSetWriter;
        Mockito.when(row.getFormattedValue(fieldOne)).thenReturn(",,");
        Mockito.when(row.getFormattedValue(fieldTwo)).thenReturn(",\"");
        try {
            dataSetWriter = CsvDataSetWriter.open(outputStream, fields);
            dataSetWriter.writeRow(row);
            String output = outputStream.toString(StandardCharsets.UTF_8.toString());
            Assert.assertEquals(
                "If the actual and expected appear to be identical, check for null characters",
                "one,two\r\n\",,\",\",\"\"\"\r\n",
                output);
        } catch (IOException e) {
            fail(e.toString());
        }
    }
}