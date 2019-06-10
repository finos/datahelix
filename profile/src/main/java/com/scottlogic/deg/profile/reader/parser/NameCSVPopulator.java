package com.scottlogic.deg.profile.reader.parser;

import com.scottlogic.deg.profile.reader.names.NameHolder;
import com.scottlogic.deg.profile.reader.parser.CSVRecordParser;
import org.apache.commons.csv.CSVRecord;


public class NameCSVPopulator implements CSVRecordParser<NameHolder> {

    @Override
    public NameHolder convertRecord(CSVRecord element) {
        return new NameHolder(element.get(0));
    }

}
