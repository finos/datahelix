package com.scottlogic.deg.profile.reader.file.parser;

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

public interface CSVRecordParser<T> extends Function<InputStream, Set<T>> {

    default Set<T> apply(InputStream is) {
        try {
            return CSVParser.parse(is, Charset.defaultCharset(), CSVFormat.DEFAULT)
                .getRecords()
                .stream()
                .map(this::convertRecord)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    T convertRecord(CSVRecord element);
}
