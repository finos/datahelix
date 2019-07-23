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

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.scottlogic.deg.output.FileUtils;
import com.scottlogic.deg.output.FileUtilsImpl;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.manifest.JsonManifestWriter;
import com.scottlogic.deg.output.manifest.ManifestWriter;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.writer.OutputWriterFactory;
import com.scottlogic.deg.output.writer.sql.SqlOutputWriterFactory;

import java.nio.file.Path;

public class OutputModule extends AbstractModule {
    private final OutputConfigSource outputConfigSource;

    public OutputModule(OutputConfigSource outputConfigSource) {
        this.outputConfigSource = outputConfigSource;
    }

    @Override
    protected void configure() {
        bind(OutputConfigSource.class).toInstance(outputConfigSource);

        bind(OutputWriterFactory.class).toProvider(OutputWriterFactoryProvider.class);
        bind(SingleDatasetOutputTarget.class).toProvider(SingleDatasetOutputTargetProvider.class);
        bind(ManifestWriter.class).to(JsonManifestWriter.class);
        bind(FileUtils.class).to(FileUtilsImpl.class);

        bind(SqlOutputWriterFactory.class).toInstance(new SqlOutputWriterFactory(outputConfigSource.getOutputTableName()));
        bind(OutputPath.class).toInstance(new OutputPath(outputConfigSource.getOutputPath()));

        bind(boolean.class)
            .annotatedWith(Names.named("config:canOverwriteOutputFiles"))
            .toInstance(outputConfigSource.overwriteOutputFiles());
    }
}
