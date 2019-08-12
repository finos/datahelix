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

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.profile.reader.ValidatingProfileReader;

import java.io.IOException;

public class CucumberValidatingProfileReader implements ValidatingProfileReader {
    @Override
    public Profile read() throws IOException {
        return null;
    }
}
