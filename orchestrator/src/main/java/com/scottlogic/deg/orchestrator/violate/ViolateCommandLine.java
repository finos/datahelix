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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.deg.orchestrator.generate.GenerateCommandLine;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
    name = "violate",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class ViolateCommandLine extends GenerateCommandLine implements ViolateConfigSource {

    @Override
    public Integer call() throws IOException {
        printAlphaFeatureWarning("VIOLATE command");

        Module container = new ViolateModule(this);
        Injector injector = Guice.createInjector(container);

        injector.getInstance(ViolateExecute.class).execute();
        return 0;
    }

    @CommandLine.Option(
        names = {"--dont-violate"},
        arity = "0..",
        description = "Choose types of constraint should not be violated")
    private List<AtomicConstraintType> constraintsToNotViolate;

    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return constraintsToNotViolate;
    }

}
