package com.scottlogic.deg.profile.reader.names;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class NameCSVPopulator {

    public Set<NameHolder> readFromFile(InputStream is) {
        try {
            return CSVParser.parse(
                is,
                Charset.defaultCharset(),
                CSVFormat.DEFAULT)
                .getRecords()
                .stream()
                .map(this::populateWithRecord)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private NameHolder populateWithRecord(CSVRecord record) {
        return new NameHolder(record.get(0));
    }

}
