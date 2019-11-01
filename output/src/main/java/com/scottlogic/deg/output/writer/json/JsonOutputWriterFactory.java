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

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.output.writer.OutputWriterFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class JsonOutputWriterFactory implements OutputWriterFactory {
    private boolean useNdJson;
    private static final String NEW_LINE_DELIMITER = "\n";
    @Inject
    public JsonOutputWriterFactory(@Named("config:useNdJson") boolean useNdJson) {
        this.useNdJson = useNdJson;
    }

    @Override
    public DataSetWriter createWriter(OutputStream stream, Fields fields) throws IOException {
        ObjectWriter objectWriter = new ObjectMapper().writer(new DefaultPrettyPrinter(NEW_LINE_DELIMITER));
        SequenceWriter writer = objectWriter.writeValues(stream);
        writer.init(!useNdJson);

        return new JsonDataSetWriter(writer, fields);
    }

    @Override
    public Optional<String> getFileExtensionWithoutDot() {
        return Optional.of("json");
    }
}
