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
import com.scottlogic.datahelix.generator.common.profile.InSetRecord;

import java.io.*;
import java.util.List;

public class CsvFileInputReader implements CsvInputReader {
    private final File path;

    public CsvFileInputReader(File path) {
        this.path = path;
    }

    public List<InSetRecord> retrieveInSetElements() {
        try (InputStream stream = createStream()) {
            return new CsvStreamInputReader(stream, path.getName()).retrieveInSetElements();
        } catch (IOException exc){
            throw new UncheckedIOException(exc);
        }
    }

    public List<String> retrieveLinesForColumn(String key) {
        try (InputStream stream = createStream()) {
            return new CsvStreamInputReader(stream, path.getName()).retrieveLinesForColumn(key);
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
