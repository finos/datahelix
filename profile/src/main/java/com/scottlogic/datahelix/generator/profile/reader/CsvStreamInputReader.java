package com.scottlogic.datahelix.generator.profile.reader;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvStreamInputReader implements CsvInputReader {
    private final InputStream stream;
    private final String path;

    public CsvStreamInputReader(InputStream stream, String path) {
        this.stream = stream;
        this.path = path;
    }

    public DistributedList<String> retrieveLines() {
        List<CSVRecord> records = parse(stream);
        return new DistributedList<>(records.stream()
            .map(this::createWeightedElementFromRecord)
            .collect(Collectors.toList()));
    }

    public DistributedList<String> retrieveLines(String key) {
        List<CSVRecord> records = parse(stream);

        int index = getIndexForKey(records.get(0), key);

        //Remove the header
        records.remove(0);

        return new DistributedList<>(records.stream()
            .map(record -> record.get(index))
            .map(record -> createWeightedElement(record, Optional.empty()))
            .collect(Collectors.toList()));
    }

    private static int getIndexForKey(CSVRecord header, String key) {
        int index = 0;
        for (String title : header) {
            if (title.equals(key)) {
                return index;
            }
            index++;
        }
        throw new ValidationException("unable to find data for key " + key);
    }

    private WeightedElement<String> createWeightedElementFromRecord(CSVRecord record) {
        try {
            Optional<Double> weighting = record.size() == 1
                ? Optional.empty()
                : Optional.of(Double.parseDouble(record.get(1)));

            return createWeightedElement(record.get(0), weighting);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                "Weighting '" + record.get(1) + "' is not a valid number\n" +
                "CSV lines containing 2 columns must hold a weighting (double) in the second column, e.g. <value>,0.5\n" +
                "Value: '" + record.get(0) + "', File: '" + this.path + "', Line " + record.getRecordNumber(), e);
        }
    }

    private static WeightedElement<String> createWeightedElement(String element, Optional<Double> weight) {
        return weight.map(integer -> new WeightedElement<>(element, integer))
            .orElseGet(() -> WeightedElement.withDefaultWeight(element));
    }

    private static List<CSVRecord> parse(InputStream stream) {
        try {
            CSVParser parser = CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.DEFAULT);
            return parser.getRecords();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
