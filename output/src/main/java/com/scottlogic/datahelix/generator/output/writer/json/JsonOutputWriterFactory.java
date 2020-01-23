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

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.output.writer.DataSetWriter;
import com.scottlogic.datahelix.generator.output.writer.OutputWriterFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class JsonOutputWriterFactory implements OutputWriterFactory {
    private boolean streamOutput;
    private static final String NEW_LINE_DELIMITER = "\n";

    @Inject
    public JsonOutputWriterFactory(@Named("config:streamOutput") boolean streamOutput) {
        this.streamOutput = streamOutput;
    }

    @Override
    public DataSetWriter createWriter(OutputStream stream, Fields fields) throws IOException {
        PrettyPrinter prettyPrinter = streamOutput
            ? new MinimalPrettyPrinter(NEW_LINE_DELIMITER)
            : new DefaultPrettyPrinter();
        ObjectWriter objectWriter = new ObjectMapper().writer(prettyPrinter);
        SequenceWriter writer = objectWriter.writeValues(stream);
        writer.init(!streamOutput);

        return new JsonDataSetWriter(writer, fields);
    }

    @Override
    public Optional<String> getFileExtensionWithoutDot() {
        return Optional.of("json");
    }
}
