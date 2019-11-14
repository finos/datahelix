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
package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.deg.profile.reader.FileReader;

import javax.inject.Inject;

public class CucumberFileReader extends FileReader {
    private final CucumberTestState testState;

    @Inject
    public CucumberFileReader(CucumberTestState testState) {
        super("");
        this.testState = testState;
    }

    @Override
    public DistributedList<String> listFromMapFile(String file, String key) {
        return DistributedList.uniform(testState.getValuesFromMap(file, key));
    }

}

