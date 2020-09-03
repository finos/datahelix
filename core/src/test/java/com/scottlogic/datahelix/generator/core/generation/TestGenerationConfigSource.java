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
package com.scottlogic.datahelix.generator.core.generation;

import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType;
import com.scottlogic.datahelix.generator.core.config.detail.MonitorType;
import com.scottlogic.datahelix.generator.core.config.detail.VisualiserLevel;

import java.nio.file.Path;

public class TestGenerationConfigSource implements GenerationConfigSource
{

    private DataGenerationType generationType;
    private CombinationStrategyType combinationStrategyType;
    private Long maxRows;
    private boolean infiniteOutput;
    private MonitorType monitorType;
    private VisualiserLevel visualiserLevel;
    private Path visualiserOutputFolder;

    public TestGenerationConfigSource(DataGenerationType generationType,
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

    @Override
    public DataGenerationType getGenerationType()
    {
        return generationType;
    }

    @Override
    public CombinationStrategyType getCombinationStrategyType()
    {
        return combinationStrategyType;
    }

    @Override
    public Long getMaxRows()
    {
        return maxRows;
    }

    @Override
    public boolean getInfiniteOutput()
    {
        return infiniteOutput;
    }

    @Override
    public MonitorType getMonitorType()
    {
        return monitorType;
    }

    @Override
    public VisualiserLevel getVisualiserLevel()
    {
        return visualiserLevel;
    }

    @Override
    public Path getVisualiserOutputFolder()
    {
        return visualiserOutputFolder;
    }
}
