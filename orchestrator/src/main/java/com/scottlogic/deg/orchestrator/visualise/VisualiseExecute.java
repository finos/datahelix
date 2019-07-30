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

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.validators.ContradictionDecisionTreeValidator;
import com.scottlogic.deg.orchestrator.validator.VisualisationConfigValidator;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class performs the visualisation of a profile
 * and outputs the visualisation as a graphviz dot file..
 */
public class VisualiseExecute {

    private final DecisionTreeFactory profileAnalyser;
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;
    private final Path outputPath;
    private final ProfileReader profileReader;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final AllConfigSource configSource;
    private final VisualisationConfigValidator validator;

    @Inject
    public VisualiseExecute(DecisionTreeFactory profileAnalyser,
                            FieldSpecFactory fieldSpecFactory,
                            FieldSpecMerger fieldSpecMerger,
                            OutputPath outputPath,
                            ProfileReader profileReader,
                            ProfileSchemaValidator profileSchemaValidator,
                            AllConfigSource configSource,
                            VisualisationConfigValidator validator) {
        this.profileAnalyser = profileAnalyser;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
        this.configSource = configSource;
        this.outputPath = outputPath.getPath();
        this.profileReader = profileReader;
        this.profileSchemaValidator = profileSchemaValidator;
        this.validator = validator;
    }

    public void execute() throws IOException {
        validator.validateCommandLine(configSource.overwriteOutputFiles(), outputPath);

        final Profile profile = profileReader.read(configSource.getProfileFile().toPath());
        profileSchemaValidator.validateProfile(configSource.getProfileFile(), profile.getSchemaVersion());

        final DecisionTree mergedTree = profileAnalyser.analyse(profile);

        final String profileBaseName = configSource.getProfileFile().getName()
            .replaceFirst("\\.[^.]+$", "");

        ContradictionDecisionTreeValidator treeValidator =
            new ContradictionDecisionTreeValidator(
                new RowSpecMerger(fieldSpecMerger),
                new ConstraintReducer(fieldSpecFactory, fieldSpecMerger));

        DecisionTree validatedTree = treeValidator.markContradictions(mergedTree);

        final String title =
            Stream.of(profile.getDescription(), profileBaseName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        writeTreeTo(
            validatedTree,
            title,
            configSource.getOutputPath());
    }

    private void writeTreeTo(
        DecisionTree decisionTree,
        String description,
        Path outputFilePath)
        throws IOException {

        try (OutputStreamWriter outWriter = new OutputStreamWriter(
            new FileOutputStream(outputFilePath.toString()),
            StandardCharsets.UTF_8)) {

            new DecisionTreeVisualisationWriter(outWriter).writeDot(
                decisionTree,
                "tree",
                description);
        }
    }
}
