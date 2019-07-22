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

package com.scottlogic.deg.generator.fieldspecs.whitelist;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FrequencyDistributedSetTest {

    @Test
    public void testEmptyIsEmpty() {
        DistributedSet<String> empty = FrequencyDistributedSet.empty();
        DistributedSet<String> manualEmpty = new FrequencyDistributedSet<>(Collections.emptySet());

        assertEquals(manualEmpty, empty);
    }

    @Test
    public void testNullSetIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FrequencyDistributedSet<>(Collections.singleton(null)));
    }

    @Test
    public void testUniformGeneratesUniformDistribution() {
        final double uniformWeight = 10.0D;
        WeightedElement<String> first = new WeightedElement<>("first", uniformWeight);
        WeightedElement<String> second = new WeightedElement<>("second", uniformWeight);
        WeightedElement<String> third = new WeightedElement<>("third", uniformWeight);

        Set<WeightedElement<String>> weightedElements = SetUtils.setOf(first, second, third);

        DistributedSet<String> manualSet = new FrequencyDistributedSet<>(weightedElements);

        Set<String> elements = SetUtils.setOf("first", "second", "third");
        DistributedSet<String> uniformSet = FrequencyDistributedSet.uniform(elements);

        assertEquals(manualSet, uniformSet);
    }

    private DistributedSet<String> prepareTwoElementSet() {
        Set<WeightedElement<String>> holders = Stream.of("first", "second", "third", "fourth")
            .map(WeightedElement::withDefaultWeight)
            .collect(Collectors.toSet());
        return new FrequencyDistributedSet<>(holders);
    }

    @Test
    public void testRandomPick() {
        DistributedSet<String> set = prepareTwoElementSet();

        String firstValue = set.pickRandomly(mockOfRandom(0.0D));
        String otherFirstValue = set.pickRandomly(mockOfRandom(0.24D));
        String secondValue = set.pickRandomly(mockOfRandom(0.25D));
        String otherSecondValue = set.pickRandomly(mockOfRandom(0.49D));
        String thirdValue = set.pickRandomly(mockOfRandom(0.5D));
        String otherThirdValue = set.pickRandomly(mockOfRandom(0.74D));
        String fourthValue = set.pickRandomly(mockOfRandom(0.75D));
        String otherFourthValue = set.pickRandomly(mockOfRandom(0.99D));

        assertEquals(firstValue, otherFirstValue);
        assertEquals(secondValue, otherSecondValue);
        assertEquals(thirdValue, otherThirdValue);
        assertEquals(fourthValue, otherFourthValue);
    }

    private static RandomNumberGenerator mockOfRandom(double value) {
        RandomNumberGenerator generator = mock(RandomNumberGenerator.class);
        when(generator.nextDouble(0.0D, 1.0D)).thenReturn(value);
        return generator;
    }
}
