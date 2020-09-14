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
package com.scottlogic.datahelix.generator.core.builders;

import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType;
import com.scottlogic.datahelix.generator.core.config.detail.MonitorType;
import com.scottlogic.datahelix.generator.core.config.detail.VisualiserLevel;
import com.scottlogic.datahelix.generator.core.generation.GenerationConfigSource;
import com.scottlogic.datahelix.generator.core.generation.TestGenerationConfigSource;

import java.io.File;
import java.nio.file.Path;

public class GenerationConfigSourceBuilder
{
    private final DataGenerationType generationType;
    private final CombinationStrategyType combinationStrategyType;
    private final Long maxRows;
    private final boolean infiniteOutput;
    private final MonitorType monitorType;
    private final VisualiserLevel visualiserLevel;
    private final Path visualiserOutputFolder;

    private GenerationConfigSourceBuilder(DataGenerationType generationType,
                                          CombinationStrategyType combinationStrategyType,
                                          Long maxRows,
                                          boolean infiniteOutput,
                                          MonitorType monitorType,
                                          VisualiserLevel visualiserLevel,
                                          Path visualiserOutputFolder)
    {
        this.generationType = generationType;
        this.combinationStrategyType = combinationStrategyType;
        this.maxRows = maxRows;
        this.infiniteOutput = infiniteOutput;
        this.monitorType = monitorType;
        this.visualiserLevel = visualiserLevel;
        this.visualiserOutputFolder = visualiserOutputFolder;
    }

    public GenerationConfigSourceBuilder()
    {
        this(DataGenerationType.RANDOM,
            CombinationStrategyType.MINIMAL,
            null,
            false,
            MonitorType.QUIET,
            VisualiserLevel.OFF,
            new File("mockFilePath").toPath());
    }

    public GenerationConfigSource build()
    {
        return new TestGenerationConfigSource(generationType,
            combinationStrategyType,
            maxRows,
            infiniteOutput,
            monitorType,
            visualiserLevel,
            visualiserOutputFolder);
    }

    public GenerationConfigSourceBuilder withCombinationStrategyType(CombinationStrategyType combinationStrategyType)
    {
        return new GenerationConfigSourceBuilder(generationType,
            combinationStrategyType,
            maxRows,
            infiniteOutput,
            monitorType,
            visualiserLevel,
            visualiserOutputFolder);
    }
}
