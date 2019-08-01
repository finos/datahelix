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

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.config.detail.*;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.orchestrator.violate.ViolateConfigSource;
import com.scottlogic.deg.output.guice.OutputFormat;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class CucumberGenerationConfigSource implements AllConfigSource, ViolateConfigSource {
    private final CucumberTestState state;

    @Inject
    public CucumberGenerationConfigSource(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public boolean requireFieldTyping() {
        return state.requireFieldTyping;
    }

    @Override
    public DataGenerationType getGenerationType() {
        return state.dataGenerationType;
    }

    @Override
    public CombinationStrategyType getCombinationStrategyType() {
        return state.combinationStrategyType;
    }

    @Override
    public TreeWalkerType getWalkerType() {
        return state.walkerType;
    }

    @Override
    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return state.getConstraintsToNotViolate();
    }

    @Override
    public MonitorType getMonitorType() {
        return MonitorType.QUIET;
    }

    @Override
    public long getMaxRows() {
        return state.maxRows;
    }

    @Override
    public boolean shouldDoPartitioning() {
        return false;
    }

    @Override
    public boolean dontOptimise() {
        return false;
    }

    @Override
    public Path getOutputPath() {
        return new File("mockFilePath").toPath();
    }

    @Override
    public File getProfileFile() {
        return new File("mockFilePath");
    }

    @Override
    public boolean isSchemaValidationDisabled() {
        return true;
    }

    @Override
    public boolean overwriteOutputFiles() {
        return false;
    }

    @Override
    public boolean useStdOut() {
        return false;
    }

    @Override
    public boolean visualiseReductions() {
        return false;
    }

    @Override
    public OutputFormat getOutputFormat() {
        return null;
    }

    @Override
    public String fromFilePath() { return null; }
}
