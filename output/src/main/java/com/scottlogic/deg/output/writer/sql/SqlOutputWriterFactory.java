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

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.output.writer.OutputWriterFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SqlOutputWriterFactory implements OutputWriterFactory {
    private final String tableName;

    public SqlOutputWriterFactory(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public DataSetWriter createWriter(OutputStream stream, ProfileFields profileFields) {
        final Writer outputStreamWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        return new SqlDataSetWriter(outputStreamWriter, profileFields, tableName);
    }

    @Override
    public Optional<String> getFileExtensionWithoutDot() {
        return Optional.of("sql");
    }
}
