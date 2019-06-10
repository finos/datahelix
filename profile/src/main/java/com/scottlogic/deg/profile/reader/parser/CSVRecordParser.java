package com.scottlogic.deg.profile.reader.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;

@FunctionalInterface
public interface CSVRecordParser<T> {

    default Set<T> readFromFile(InputStream is) {
        try {
            return org.apache.commons.csv.CSVParser.parse(
                is,
                Charset.defaultCharset(),
                CSVFormat.DEFAULT)
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
