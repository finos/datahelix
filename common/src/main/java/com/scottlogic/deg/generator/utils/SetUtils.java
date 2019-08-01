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

package com.scottlogic.deg.generator.utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetUtils {
    public static <T> Set<T> union(Collection<T> a, Collection<T> b) {
        return Collections.unmodifiableSet(Stream
            .concat(a.stream(), b.stream())
            .collect(Collectors.toSet()));
    }

    public static <T> Set<T> intersect(Collection<T> a, Collection<T> b) {
        final Set<T> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        return Collections.unmodifiableSet(intersection);
    }

    public static <T> Set<T> setOf(T e1, T e2, T e3) {
        return Collections.unmodifiableSet(Stream.of(e1, e2, e3).collect(Collectors.toSet()));
    }

    public static <T> Set<T> setOf(T... elements) {
        return Collections.unmodifiableSet(Arrays.stream(elements).collect(Collectors.toSet()));
    }

}
