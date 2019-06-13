package com.scottlogic.deg.profile.reader.file.parser;

import org.apache.commons.csv.CSVRecord;

public class StringCSVPopulator implements CSVRecordParser<String> {

    @Override
    public String convertRecord(CSVRecord element) {
        return element.get(0);
    }

}
