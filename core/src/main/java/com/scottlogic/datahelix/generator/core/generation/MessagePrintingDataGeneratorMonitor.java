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

import com.scottlogic.datahelix.generator.common.output.GeneratedObject;

import java.io.PrintWriter;

public class MessagePrintingDataGeneratorMonitor extends AbstractDataGeneratorMonitor {
    public MessagePrintingDataGeneratorMonitor(PrintWriter writer) {
        super(writer);
    }

    private void println(String message) {
        writer.println(message);
    }

    @Override
    public void rowEmitted(GeneratedObject item) {
        println("RowSpec emitted");
    }

}
