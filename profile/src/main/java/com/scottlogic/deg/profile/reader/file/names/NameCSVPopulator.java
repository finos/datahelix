package com.scottlogic.deg.profile.reader.file.names;

import com.scottlogic.deg.profile.reader.file.parser.CSVRecordParser;
import org.apache.commons.csv.CSVRecord;


public class NameCSVPopulator implements CSVRecordParser<NameHolder> {

    @Override
    public NameHolder convertRecord(CSVRecord element) {
        return new NameHolder(element.get(0));
    }

}
