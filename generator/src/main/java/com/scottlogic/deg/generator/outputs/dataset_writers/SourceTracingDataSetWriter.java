package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.*;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

public class SourceTracingDataSetWriter implements IDataSetWriter<SourceTracingDataSetWriter.JsonArrayOutputWriter> {
    private final ObjectWriter writer;

    public SourceTracingDataSetWriter() {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        writer = jsonObjectMapper.writer();
    }

    @Override
    public JsonArrayOutputWriter openWriter(Path directory, String filenameWithoutExtension, ProfileFields profileFields) throws IOException {
        File file = directory.resolve(filenameWithoutExtension + "-trace.json").toFile();
        return new JsonArrayOutputWriter(new PrintWriter(new FileOutputStream(file, false)));
    }

    @Override
    public void writeRow(JsonArrayOutputWriter closeable, GeneratedObject row) throws IOException {
        TracingDto dto = new TracingDto(Collections.emptySet(), null); //TODO: get rule & set of constraints
        closeable.writeArrayItem(serialise(dto));
    }

    private String serialise(Object value) throws JsonProcessingException {
        return writer.writeValueAsString(value);
    }

    class TracingDto {
        public Set<IConstraint> constraints;
        public String rule;

        TracingDto(Set<IConstraint> constraints, String rule) {
            this.constraints = constraints;
            this.rule = rule;
        }
    }

    class JsonArrayOutputWriter implements Closeable {
        private final PrintWriter writer;
        private boolean firstItemInArrayWritten = false;

        JsonArrayOutputWriter(PrintWriter writer) {
            this.writer = writer;
            this.writer.println("[");
        }

        @Override
        public void close() {
            this.writer.println("]");
            this.writer.close();
        }

        void writeArrayItem(String jsonString) {
            if (firstItemInArrayWritten){
                this.writer.print(",");
                this.writer.println();
            }

            this.writer.print(" " + jsonString);
            firstItemInArrayWritten = true;
            this.writer.flush();
        }
    }
}
