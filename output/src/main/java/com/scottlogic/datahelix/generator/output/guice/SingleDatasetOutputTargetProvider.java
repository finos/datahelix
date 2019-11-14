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

package com.scottlogic.datahelix.generator.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.datahelix.generator.output.outputtarget.FileOutputTarget;
import com.scottlogic.datahelix.generator.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.datahelix.generator.output.outputtarget.StdoutOutputTarget;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final OutputConfigSource outputConfigSource;
    private final FileOutputTarget fileOutputTarget;
    private final StdoutOutputTarget stdoutOutputTarget;

    @Inject
    SingleDatasetOutputTargetProvider(
        OutputConfigSource outputConfigSource,
        FileOutputTarget fileOutputTarget,
        StdoutOutputTarget stdoutOutputTarget){
        this.outputConfigSource = outputConfigSource;
        this.fileOutputTarget = fileOutputTarget;
        this.stdoutOutputTarget = stdoutOutputTarget;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        if (outputConfigSource.useStdOut()){
            return stdoutOutputTarget;
        }
        return fileOutputTarget;
    }
}
