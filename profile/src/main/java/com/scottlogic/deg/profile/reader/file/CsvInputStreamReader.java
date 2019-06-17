package com.scottlogic.deg.profile.reader.file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CsvInputStreamReader {

    private CsvInputStreamReader() {
        throw new UnsupportedOperationException("No instantiation of static class");
    }

    public static Set<String> retrieveLines(InputStream stream) {
        Set<String> result = extractLines(stream);
        closeQuietly(stream);
        return result;
    }

    private static Set<String> extractLines(InputStream stream) {
        List<CSVRecord> records = parse(stream);

        Set<String> firstElementFromEachRecord = new HashSet<>();
        for (CSVRecord record : records) {
            String firstElement = firstElementFromRecord(record);
            firstElementFromEachRecord.add(firstElement);
        }

        return firstElementFromEachRecord;
    }

    private static List<CSVRecord> parse(InputStream stream) {
        try {
            CSVParser parser = CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.DEFAULT);
            return parser.getRecords();
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
