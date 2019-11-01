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

package com.scottlogic.deg.output.outputtarget;

import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.output.writer.DataSetWriter;

import java.io.IOException;

public interface SingleDatasetOutputTarget {
    DataSetWriter openWriter(Fields fields) throws IOException;
    default void validate() throws OutputTargetValidationException, IOException {}
}
