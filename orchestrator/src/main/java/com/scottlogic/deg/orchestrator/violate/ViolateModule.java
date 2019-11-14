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

package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.datahelix.generator.core.violations.filters.ViolationFilter;
import com.scottlogic.deg.orchestrator.guice.AllModule;

import java.util.List;

public class ViolateModule extends AbstractModule {
    private final ViolateConfigSource configSource;

    public ViolateModule(ViolateConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    protected void configure() {
        bind(AllConfigSource.class).toInstance(configSource);
        bind(ViolateConfigSource.class).toInstance(configSource);

        bind(new TypeLiteral<List<ViolationFilter>>(){}).toProvider(ViolationFiltersProvider.class);

        install(new AllModule(configSource));
    }
}
