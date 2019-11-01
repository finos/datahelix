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

package com.scottlogic.deg.profile.reader.file.names;


import com.scottlogic.deg.generator.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.profile.reader.NameRetriever;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class NameRetrieverTest {

    @Test
    public void testLoadingFirstNames() {
        DistributedList<Object> names = NameRetriever.loadNamesFromFile(NameConstraintTypes.FIRST);

        assertEquals(704, names.distributedList().size());
    }

    @Test
    public void testLoadingLastNames() {
        DistributedList<Object> names = NameRetriever.loadNamesFromFile(NameConstraintTypes.LAST);

        assertEquals(280, names.distributedList().size());
    }

    @Test
    public void testLoadingFullNames() {
        DistributedList<Object> names = NameRetriever.loadNamesFromFile(NameConstraintTypes.FULL);

        assertEquals(197120, names.distributedList().size());
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        DistributedList<Object> result = NameRetriever.loadNamesFromFile(config);

        assertNotNull(result);
    }
}