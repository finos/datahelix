package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.outputs.CellSource;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.RowSource;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class SourceTracingDataSetWriter implements DataSetWriter<SourceTracingDataSetWriter.JsonArrayOutputWriter> {
    private final ObjectWriter writer;

    public SourceTracingDataSetWriter() {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        writer = jsonObjectMapper.writerWithDefaultPrettyPrinter();
    }

    @Override
    public JsonArrayOutputWriter openWriter(Path directory, String filenameWithoutExtension, ProfileFields profileFields) throws IOException {
        File file = directory.resolve(filenameWithoutExtension + "-trace.json").toFile();
        return new JsonArrayOutputWriter(new PrintWriter(new FileOutputStream(file, false)));
    }

    @Override
    public void writeRow(JsonArrayOutputWriter closeable, GeneratedObject row) throws IOException {
        Collection<TracingDto> dto = row.source != null
            ? TracingDto.fromRowSource(row.source)
            : TracingDto.empty;
        closeable.writeArrayItem(serialise(dto));
    }

    private String serialise(Object value) throws JsonProcessingException {
        return writer.writeValueAsString(value);
    }

    static class TracingDto {
        public static final Collection<TracingDto> empty = Collections.emptySet();

        public Set<TracingConstraintDto> constraints;
        public Set<TracingRuleDto> rules;
        public String field;

        TracingDto(Set<TracingConstraintDto> constraints, Set<TracingRuleDto> rules, String field) {
            this.constraints = constraints;
            this.rules = rules;
            this.field = field;
        }

        static Collection<TracingDto> fromRowSource(RowSource rowSource) {
            return rowSource.columns
                .stream()
                .map(TracingDto::fromCellSource)
                .collect(Collectors.toList());
        }

        private static TracingDto fromCellSource(CellSource cellSource) {
            return new TracingDto(
                cellSource.getConstraints().stream().map(TracingConstraintDto::new).collect(Collectors.toSet()),
                cellSource.getRules().stream().map(TracingRuleDto::new).collect(Collectors.toSet()),
                cellSource.field.name);
        }
    }

    private static class TracingRuleDto{
        public String rule;
        public boolean violated;

        TracingRuleDto(ConstraintRule rule) {
            this.rule = rule.getDescription();
            this.violated = rule.isViolated();
        }
    }

    private static class TracingConstraintDto {
        public String type;
        public String value;
        public String rule;
        public boolean violated;

        TracingConstraintDto(AtomicConstraint constraint) {
            type = constraint.getClass().getSimpleName();
            value = constraint.toString();
            rule = constraint.getRule().getDescription();
            violated = constraint.getRule().isViolated();
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
