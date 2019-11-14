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

package com.scottlogic.datahelix.generator.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.datahelix.generator.output.writer.OutputWriterFactory;
import com.scottlogic.datahelix.generator.output.writer.csv.CsvOutputWriterFactory;
import com.scottlogic.datahelix.generator.output.writer.json.JsonOutputWriterFactory;

public class OutputWriterFactoryProvider implements Provider<OutputWriterFactory> {
    private final OutputConfigSource configSource;
    private final CsvOutputWriterFactory csvOutputWriterFactory;
    private final JsonOutputWriterFactory jsonOutputWriterFactory;

    @Inject
    public OutputWriterFactoryProvider(
        OutputConfigSource configSource,
        CsvOutputWriterFactory csvOutputWriterFactory,
        JsonOutputWriterFactory jsonOutputWriterFactory)
    {
        this.configSource = configSource;
        this.csvOutputWriterFactory = csvOutputWriterFactory;
        this.jsonOutputWriterFactory = jsonOutputWriterFactory;
    }

    @Override
    public OutputWriterFactory get() {
        switch (configSource.getOutputFormat()){
            case CSV:
                return csvOutputWriterFactory;
            case JSON:
                return jsonOutputWriterFactory;
        }

        throw new RuntimeException(String.format(
            "Unknown output format %s, options are CSV or JSON",
            configSource.getOutputFormat()
        ));
    }
}
