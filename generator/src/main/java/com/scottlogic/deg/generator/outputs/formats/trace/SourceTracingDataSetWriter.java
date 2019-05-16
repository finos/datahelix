package com.scottlogic.deg.generator.outputs.formats.trace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.outputs.CellSource;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.RowSource;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class SourceTracingDataSetWriter implements DataSetWriter {
    private final SequenceWriter writer;

    private SourceTracingDataSetWriter(SequenceWriter writer) {
        this.writer = writer;
    }

    static DataSetWriter open(OutputStream stream) throws IOException {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        final ObjectWriter objectWriter = jsonObjectMapper.writerWithDefaultPrettyPrinter();

        return new SourceTracingDataSetWriter(
            objectWriter.writeValues(stream));
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        Collection<TracingDto> dto = row.source != null
            ? TracingDto.fromRowSource(row.source)
            : TracingDto.empty;

        writer.write(dto);
    }

    @Override
    public void close() throws IOException {
        writer.close();
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
}
