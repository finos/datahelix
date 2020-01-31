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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;

import java.io.*;
import java.util.stream.Collectors;

public class FileReader {
    public DistributedList<Object> setFromFile(File file) {
        InputStream streamFromPath = createStreamFromPath(file);

        DistributedList<String> names = CsvInputStreamReader.retrieveLines(streamFromPath);
        closeStream(streamFromPath);

        return new DistributedList<>(
            names.distributedList().stream()
                .map(holder -> new WeightedElement<>((Object) holder.element(), holder.weight()))
                .distinct()
                .collect(Collectors.toList()));
    }

    public DistributedList<String> listFromMapFile(File file, String key) {
        InputStream streamFromPath = createStreamFromPath(file);

        DistributedList<String> names = CsvInputStreamReader.retrieveLines(streamFromPath, key);
        closeStream(streamFromPath);

        return new DistributedList<>(
            names.distributedList().stream()
                .map(holder -> new WeightedElement<>(holder.element(), holder.weight()))
                .collect(Collectors.toList()));
    }

    private static InputStream createStreamFromPath(File path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private static void closeStream(InputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
