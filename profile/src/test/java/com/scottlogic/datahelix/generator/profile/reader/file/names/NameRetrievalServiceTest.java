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

package com.scottlogic.datahelix.generator.profile.reader.file.names;


import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.NameConstraintTypes;
import com.scottlogic.datahelix.generator.profile.reader.CsvInputStreamReaderFactory;
import com.scottlogic.datahelix.generator.profile.services.NameRetrievalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NameRetrievalServiceTest
{
    @Test
    public void testLoadingFirstNames() {
        CsvInputStreamReaderFactory csvReaderFactory = new CsvInputStreamReaderFactory();
        NameRetrievalService service = new NameRetrievalService(csvReaderFactory);

        List<WeightedElement<Object>> names = service.loadNamesFromFile(NameConstraintTypes.FIRST);

        assertEquals(704, names.size());
    }

    @Test
    public void testLoadingLastNames() {
        CsvInputStreamReaderFactory csvReaderFactory = new CsvInputStreamReaderFactory();
        NameRetrievalService service = new NameRetrievalService(csvReaderFactory);

        List<WeightedElement<Object>> names = service.loadNamesFromFile(NameConstraintTypes.LAST);

        assertEquals(280, names.size());
    }

    @Test
    public void testLoadingFullNames() {
        CsvInputStreamReaderFactory csvReaderFactory = new CsvInputStreamReaderFactory();
        NameRetrievalService service = new NameRetrievalService(csvReaderFactory);

        List<WeightedElement<Object>> names = service.loadNamesFromFile(NameConstraintTypes.FULL);

        assertEquals(197120, names.size());
    }

    @ParameterizedTest
    @EnumSource(NameConstraintTypes.class)
    public void testAllValuesGiveValidResult(NameConstraintTypes config) {
        CsvInputStreamReaderFactory csvReaderFactory = new CsvInputStreamReaderFactory();
        NameRetrievalService service = new NameRetrievalService(csvReaderFactory);

        List<WeightedElement<Object>> result = service.loadNamesFromFile(config);

        assertNotNull(result);
    }
}