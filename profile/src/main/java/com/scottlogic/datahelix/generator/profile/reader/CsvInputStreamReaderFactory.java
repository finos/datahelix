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

import com.google.inject.name.Named;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;

public class CsvInputStreamReaderFactory {
    private final String filePath;

    @Inject
    public CsvInputStreamReaderFactory(@Named("config:filePath") String filePath) {
        this.filePath = filePath.endsWith(File.separator) || filePath.isEmpty()
            ? filePath
            : filePath + File.separator;
    }

    public CsvInputReader getReaderForFile(String file) {
        return new CsvFileInputReader(filePath + file);
    }

    public CsvInputReader getReaderForStream(InputStream stream, String name) {
        return new CsvStreamInputReader(stream, name);
    }
}
