/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils;

import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.profile.reader.CsvInputStreamReaderFactory;
import com.scottlogic.datahelix.generator.profile.reader.FileReader;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CucumberFileReader extends FileReader {
    private final CucumberTestState testState;

    @Inject
    public CucumberFileReader(CucumberTestState testState) {
        super(new CsvInputStreamReaderFactory());
        this.testState = testState;
    }

    @Override
    public List<String> listFromMapFile(File file, String key) {
        return testState.getValuesFromMap(file.getName(), key);
    }
}

