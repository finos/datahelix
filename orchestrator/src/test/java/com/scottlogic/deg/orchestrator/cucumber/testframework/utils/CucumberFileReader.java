package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.profile.reader.FileReader;

import javax.inject.Inject;

public class CucumberFileReader extends FileReader
{
    private final CucumberTestState testState;

    @Inject
    public CucumberFileReader(CucumberTestState testState) {
        super("");
        this.testState = testState;
    }

    @Override
    public DistributedList<String> listFromMapFile(String file, String key)
    {
        return DistributedList.uniform(testState.getValuesFromMap(file, key));
    }

}

