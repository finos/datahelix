package com.scottlogic.datahelix.generator.profile.reader;

import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;

public interface CsvInputReader{
    DistributedList<String> retrieveLines();
    DistributedList<String> retrieveLines(String key);
}
