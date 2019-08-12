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

package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.common.util.FileUtils;
import com.scottlogic.deg.output.OutputPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class ReductiveIterationVisualiser implements IterationVisualiser {
    private final AtomicInteger currentIteration = new AtomicInteger();
    private final FileUtils fileUtils;
    private final Path visualiseDirectoryPath;

    @Inject
    public ReductiveIterationVisualiser(
        OutputPath outputPath,
        FileUtils fileUtils) {
        Path path = outputPath.getPath();

        this.fileUtils = fileUtils;
        boolean outputPathIsADirectory = path != null && path.toFile().isDirectory();

        Path directoryPath;
        if (outputPathIsADirectory) {
            directoryPath = path;
        } else {
            directoryPath = path == null || path.getParent() == null
                ? Paths.get(System.getProperty("user.dir"))
                : path.getParent();
        }

        this.visualiseDirectoryPath = directoryPath.resolve("reductive-walker");
    }

    @Override
    public void visualise(ConstraintNode rootNode, ReductiveState reductiveState) throws IOException {
        createVisualiseDirectoryIfAbsent();

        int iteration = currentIteration.getAndIncrement();

        ProfileFields profileFields = reductiveState.getFields();
        String description = String.format("Iteration %d\n%s", iteration, reductiveState.toString(true));
        Path outputPath = visualiseDirectoryPath.resolve(String.format("Reduced-tree-%03d.gv", iteration));

        //copy of Visualise.writeTreeTo()
        try (OutputStreamWriter outWriter = new OutputStreamWriter(
            new FileOutputStream(outputPath.toString()),
            StandardCharsets.UTF_8)) {

            new DecisionTreeVisualisationWriter(outWriter).writeDot(
                new DecisionTree(rootNode, profileFields),
                "tree",
                description);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createVisualiseDirectoryIfAbsent() throws IOException {
        File directory = this.visualiseDirectoryPath.toFile();
        if (directory.exists()){
            if (directory.isDirectory()){
                return; //exists
            }

            throw new IOException("Cannot visualise iterations of the tree, a file exists at the path where the" +
                " directory needs to exist.\n" + this.visualiseDirectoryPath.toString());
        }

        fileUtils.createDirectories(this.visualiseDirectoryPath);
    }
}
