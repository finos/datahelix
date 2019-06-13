package com.scottlogic.deg.profile.reader.file;

import com.scottlogic.deg.profile.reader.file.inputstream.ClasspathMapper;
import com.scottlogic.deg.profile.reader.file.inputstream.PathMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class CSVPathSetMapper implements Function<String, Stream<String>> {

    private final Function<String, InputStream> pathStreamMapper;

    public CSVPathSetMapper() {
        this(new PathMapper());
    }

    public CSVPathSetMapper(final Function<String, InputStream> pathStreamMapper) {
        this.pathStreamMapper = pathStreamMapper;
    }

    @Override
    public Stream<String> apply(String path) {
        InputStream stream = Optional.of(path)
            .map(pathStreamMapper)
            .orElseThrow(() -> new UnsupportedOperationException("Path mapper is incorrectly configured"));
        Stream<String> result = extractLines(stream);
        closeQuietly(stream);
        return result;
    }

    private static Stream<String> extractLines(InputStream stream) {
        try {
            return CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.DEFAULT)
                .getRecords()
                .stream()
                .map(CSVPathSetMapper::firstElementFromRecord);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String firstElementFromRecord(CSVRecord record) {
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
