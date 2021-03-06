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
package com.scottlogic.datahelix.generator.profile.serialisation;

import com.scottlogic.datahelix.generator.common.profile.InSetRecord;
import com.scottlogic.datahelix.generator.profile.reader.FileReader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

public class TestFileReader extends FileReader {
    private final boolean weighted;

    public TestFileReader() {
        super(null);
        weighted = false;
    }

    public TestFileReader(boolean weighted) {
        super(null);
        this.weighted = weighted;
    }

    @Override
    public List<InSetRecord> setFromFile(File file) {
        return weighted
            ? this.getDistributedListWithWeights()
            : singletonList(new InSetRecord("test"));
    }
    @Override
    public List<String> listFromMapFile(File file, String key) {
        return singletonList("test");
    }

    private static List<InSetRecord> getDistributedListWithWeights() {
        return Arrays.asList(
            new InSetRecord("test1", 20),
            new InSetRecord("test2", 80)
        );
    }
}

