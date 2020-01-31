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

package com.scottlogic.datahelix.generator.profile.reader;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;

import java.io.*;

public class CsvFileInputReader implements CsvInputReader {
    private final String path;

    public CsvFileInputReader(String path) {
        this.path = path;
    }

    public DistributedList<String> retrieveLines() {
        try (InputStream stream = createStream()) {
            return new CsvStreamInputReader(stream, path).retrieveLines();
        } catch (IOException exc){
            throw new UncheckedIOException(exc);
        }
    }

    public DistributedList<String> retrieveLines(String key) {
        try (InputStream stream = createStream()) {
            return new CsvStreamInputReader(stream, path).retrieveLines(key);
        } catch (IOException exc){
            throw new UncheckedIOException(exc);
        }
    }

    private InputStream createStream() {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
