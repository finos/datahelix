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

package com.scottlogic.datahelix.generator.custom;

import java.util.stream.Stream;

public interface CustomGenerator<T> {
    /***
     * REQUIRED! used in profile reading
     * @return the name of the custom generator
     */
    String generatorName();

    /**
     * STRING for Strings
     * NUMERIC for BigDecimals
     * DATETIME for OffsetDateTimes
     * @return accepted field type
     */
    CustomGeneratorFieldType fieldType();

    /**
     * the part of the generator to be used during random generation
     *
     * Required if you want your custom generator to support random generation
     *
     * @return your stream of random values
     */
    Stream<T> generateRandom();

    /**
     * the part of the generator to be used when the generator constraint is negated during random generation
     *  - this should be implemented as the values that your regular generator should not be outputting
     *
     * Required if you want your custom generator to support being negated in random mode
     * Required if you want your custom generator to support used in the IF part of IF THEN constraints
     *
     * @return your stream of random values that are not what would be produced by the generator if it were not negated
     */
    Stream<T> generateNegatedRandom();

    /**
     * the part of the generator to be used during sequential generation
     *
     * Required if you want your custom generator to support sequential generation
     * Required if you want your custom generator to support unique keys generation
     *
     * @return your stream of random values
     */
    Stream<T> generateSequential();

    /**
     * the part of the generator to be used when the generator constraint is negated during sequential generation
     *
     * Required if you want your custom generator to support being negated in sequential mode
     * should not be used with unique keys generation
     *
     * @return your stream of sequential values that are not what would be produced by the generator if it were not negated
     */
    Stream<T> generateNegatedSequential();

    /**
     * The function to check whether a value from a set can be generated from this generator
     *
     * Required if you want your custom generator to support being combined with inSet and equalTo constraints
     *
     * @param value the value from the set to be tested against
     *
     * @return true if the generator would be able to produce the value
     */
    boolean setMatchingFunction(T value);
}
