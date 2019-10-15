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

package com.scottlogic.deg.common.profile.constraintdetail;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

public interface Granularity<T> {

    boolean isCorrectScale(T value);

    Granularity<T> merge(Granularity<T> otherGranularity);

    T getNext(T value, int amount);

    T trimToGranularity(T value);

    T getPrevious(T value);

    default T getNext(T value){
        return getNext(value, 1);
    };

    T getRandom(T min, T max, RandomNumberGenerator randomNumberGenerator);
}
