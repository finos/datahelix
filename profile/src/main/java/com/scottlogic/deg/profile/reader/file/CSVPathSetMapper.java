package com.scottlogic.deg.profile.reader.file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVPathSetMapper implements Function<String, Set<String>> {

    private final Function<String, InputStream> pathStreamMapper;

    public CSVPathSetMapper(final Function<String, InputStream> pathStreamMapper) {
        this.pathStreamMapper = pathStreamMapper;
    }

    @Override
    public Set<String> apply(String path) {
        InputStream stream = pathStreamMapper.apply(path);
        Set<String> result = extractLines(stream);
        closeQuietly(stream);
        return result;
    }

    private Set<String> extractLines(InputStream stream) {
        try {
            return CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.DEFAULT)
                .getRecords()
                .stream()
                .map(this::firstElementFromRecord)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String firstElementFromRecord(CSVRecord record) {
        return record.get(0);
    }

    private static void closeQuietly(InputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
