package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.outputs.CellSource;
import com.scottlogic.deg.generator.outputs.RowSource;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SourceTracingDataSetWriter implements DataSetWriter<SourceTracingDataSetWriter.JsonArrayOutputWriter> {
    private final ObjectWriter writer;

    public SourceTracingDataSetWriter() {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        writer = jsonObjectMapper.writerWithDefaultPrettyPrinter();
    }

    @Override
    public JsonArrayOutputWriter openWriter(Path directory, String fileName, ProfileFields profileFields) throws IOException {
        return new JsonArrayOutputWriter(new PrintWriter(new FileOutputStream(directory.resolve(fileName).toString(), false)));
    }

    @Override
    public String getFileName(String fileNameWithoutExtension) {
        return fileNameWithoutExtension + "-trace.json";
    }

    @Override
    public void writeRow(JsonArrayOutputWriter closeable, Row row, ProfileFields profileFields) throws IOException {
        Collection<TracingDto> dto = getRowSource(row) != null
            ? TracingDto.fromRowSource(getRowSource(row))
            : TracingDto.empty;
        closeable.writeArrayItem(serialise(dto));
    }

    public RowSource getRowSource(Row row) {
        return new RowSource(
            row.getFieldToValue().keySet()
                .stream()
                .map(field -> {
                    Value value = row.getFieldToValue().get(field);
                    return new CellSource(value, field);
                })
                .collect(Collectors.toList())
        );
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
                cellSource.getConstraints().stream().map(c -> new TracingConstraintDto(c, cellSource.isViolated(c))).collect(Collectors.toSet()),
                cellSource.getRules().stream().map(rule -> new TracingRuleDto(rule, cellSource.isViolated(rule))).collect(Collectors.toSet()),
                cellSource.field.name);
        }
    }

    private static class TracingRuleDto{
        public String rule;
        public boolean violated;

        TracingRuleDto(RuleInformation rule, boolean anyConstraintInRuleIsViolated) {
            this.rule = rule.getDescription();
            this.violated = anyConstraintInRuleIsViolated;
        }
    }

    private static class TracingConstraintDto {
        public String type;
        public String value;
        public List<String> rules;
        public boolean violated;

        TracingConstraintDto(AtomicConstraint constraint, boolean violated) {
            this.type = constraint.getClass().getSimpleName();
            this.value = constraint.toString();
            this.rules = constraint.getRules()
                .stream()
                .map(RuleInformation::getDescription)
                .collect(Collectors.toList());
            this.violated = violated;
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
