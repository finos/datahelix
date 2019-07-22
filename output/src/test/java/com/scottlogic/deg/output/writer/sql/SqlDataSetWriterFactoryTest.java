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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.OutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(JUnitPlatform.class)
class SqlDataSetWriterFactoryTest {

    private SqlOutputWriterFactory factory;
    private OutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = mock(OutputStream.class);
        factory = new SqlOutputWriterFactory("TABLENAME");
    }

    @Test
    void createWriter() {
        ProfileFields profileFields = new ProfileFields(Lists.newArrayList(new Field("f1"), new Field("f2")));
        DataSetWriter res = factory.createWriter(outputStream, profileFields);
        assertNotNull(res);
    }

    @Test
    void getFileExtensionWithoutDot() {
        assertEquals(Optional.of("sql"), factory.getFileExtensionWithoutDot());
    }

}
