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

package com.scottlogic.datahelix.generator.core.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.datahelix.generator.core.config.detail.MonitorType;
import com.scottlogic.datahelix.generator.core.generation.*;

import javax.annotation.Nullable;

public class DataGeneratorProvider implements Provider<DataGenerator> {
    private final DataGenerator coreGenerator;
    private final Long maxRows;
    private final MonitorType monitorType;
    private final DataGeneratorMonitor monitor;

    @Inject
    public DataGeneratorProvider(
        DecisionTreeDataGenerator coreGenerator,
        @Nullable @Named("config:maxRows") Long maxRows,
        MonitorType monitorType,
        DataGeneratorMonitor monitor) {
        this.coreGenerator = coreGenerator;
        this.maxRows = maxRows;
        this.monitorType = monitorType;
        this.monitor = monitor;
    }

    @Override
    public DataGenerator get() {
        DataGenerator limitingGenerator = maxRows == null
            ? coreGenerator
            : new LimitingDataGenerator(coreGenerator, maxRows);

        if (monitorType == MonitorType.QUIET){
            return limitingGenerator;
        }

        return new MonitoringDataGenerator(
            limitingGenerator,
            monitor);
    }
}
