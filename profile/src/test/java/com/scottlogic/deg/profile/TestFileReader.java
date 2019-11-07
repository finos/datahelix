package com.scottlogic.deg.profile;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.profile.reader.FileReader;

import java.util.Collections;

public class TestFileReader extends FileReader {

    public TestFileReader() {
        super("");
    }

    @Override
    public DistributedList<Object> setFromFile(String file) {
        return DistributedList.uniform(Collections.singleton("test"));
    }
    @Override
    public DistributedList<String> listFromMapFile(String file, String key) {
        return DistributedList.uniform(Collections.singleton("test"));
    }

}

