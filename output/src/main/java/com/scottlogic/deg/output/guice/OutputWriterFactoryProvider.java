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

package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.output.writer.OutputWriterFactory;
import com.scottlogic.deg.output.writer.csv.CsvOutputWriterFactory;
import com.scottlogic.deg.output.writer.json.JsonOutputWriterFactory;
import com.scottlogic.deg.output.writer.sql.SqlOutputWriterFactory;

public class OutputWriterFactoryProvider implements Provider<OutputWriterFactory> {
    private final OutputConfigSource configSource;
    private final CsvOutputWriterFactory csvOutputWriterFactory;
    private final JsonOutputWriterFactory jsonOutputWriterFactory;
    private final SqlOutputWriterFactory sqlOutputWriterFactory;

    @Inject
    public OutputWriterFactoryProvider(
        OutputConfigSource configSource,
        CsvOutputWriterFactory csvOutputWriterFactory,
        JsonOutputWriterFactory jsonOutputWriterFactory,
        SqlOutputWriterFactory sqlOutputWriterFactory)
    {
        this.configSource = configSource;
        this.csvOutputWriterFactory = csvOutputWriterFactory;
        this.jsonOutputWriterFactory = jsonOutputWriterFactory;
        this.sqlOutputWriterFactory = sqlOutputWriterFactory;
    }

    @Override
    public OutputWriterFactory get() {
        switch (configSource.getOutputFormat()){
            case CSV:
                return csvOutputWriterFactory;
            case JSON:
                return jsonOutputWriterFactory;
            case SQL:
                return sqlOutputWriterFactory;

        }

        throw new RuntimeException(String.format(
            "Unknown output format %s, options are CSV or JSON or SQL",
            configSource.getOutputFormat()
        ));
    }
}
