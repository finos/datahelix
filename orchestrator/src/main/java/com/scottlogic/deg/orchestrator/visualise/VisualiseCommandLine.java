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

package com.scottlogic.deg.orchestrator.visualise;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.deg.orchestrator.generate.GenerateCommandLine;
import com.scottlogic.deg.orchestrator.guice.AllModule;
import picocli.CommandLine;

import java.io.IOException;


/**
 * This class holds the visualisation specific command line options.
 *
 * @see <a href="https://github.com/finos/datahelix/blob/master/docs/Options/VisualiseOptions.md">
 * Visualise options</a> for more details.
 */
@CommandLine.Command(
    name = "visualise",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class VisualiseCommandLine extends GenerateCommandLine {

    @Override
    public Integer call() throws IOException {
        printAlphaFeatureWarning("VISUALISE command");

        Module container = new AllModule(this);
        Injector injector = Guice.createInjector(container);

        injector.getInstance(VisualiseExecute.class).execute();
        return 0;
    }
}
