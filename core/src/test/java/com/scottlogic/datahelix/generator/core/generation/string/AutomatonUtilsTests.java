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

package com.scottlogic.datahelix.generator.core.generation.string;

import dk.brics.automaton.Automaton;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutomatonUtilsTests {

    @Test
    public void createAutomaton_withValidString_shouldAcceptValidCharacters(){
        String validRegex = ".*";
        Map<String, Automaton> dummyCache = new HashMap<>();

        Automaton automaton = AutomatonUtils.createAutomaton(validRegex, true, dummyCache);

        assertTrue(automaton.run("a"));
    }

    @Test
    public void createAutomaton_withValidString_shouldRejectInvalidCharacters(){
        String validRegex = ".*";
        Map<String, Automaton> dummyCache = new HashMap<>();

        Automaton automaton = AutomatonUtils.createAutomaton(validRegex, true, dummyCache);

        assertFalse(automaton.run("汉字"));
    }

    @Test
    public void createAutomaton_withInValidString_shouldCreateEmptyAutomaton(){
        String validRegex = "汉字*";
        Map<String, Automaton> dummyCache = new HashMap<>();

        Automaton automaton = AutomatonUtils.createAutomaton(validRegex, true, dummyCache);

        assertTrue(automaton.isEmpty());
    }
}