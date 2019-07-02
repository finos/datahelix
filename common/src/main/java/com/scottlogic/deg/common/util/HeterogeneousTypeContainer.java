Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.common.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A mapping of classes to an instance of the given class
 *
 * @param <I> Interface/Class from which all instances in the container must extend
 */
public class HeterogeneousTypeContainer<I> {

    private final Map<Class<? extends I>, I> map;

    public HeterogeneousTypeContainer() {
        this.map = new HashMap<>();
    }

    private HeterogeneousTypeContainer(Map<Class<? extends I>, I> map) {
        this.map = map;
    }

    /**
     * Put an instance into the container
     *
     * @param type The type of the instance being put in
     * @param element The instance being put in
     * @param <T> The specific type of the instance (ie., type)
     * @return New container with the value input (original container is unaffected)
     */
    public <T extends I> HeterogeneousTypeContainer<I> put(Class<T> type, T element) {
        Map<Class<? extends I>, I> copy = new HashMap<>(map);
        if (element != null) {
            copy.put(Objects.requireNonNull(type), element);
        } else {
            copy.remove(Objects.requireNonNull(type));
        }
        return new HeterogeneousTypeContainer<>(copy);
    }

    /**
     * Gets instance for type
     *
     * @param type Subtype of I
     * @param <T> Type of instance
     * @return Optional of type, empty if no mapping exists
     */
    public <T extends I> Optional<T> get(Class<T> type) {
        return Optional.ofNullable(map.getOrDefault(type, null)).map(type::cast);
    }

    /**
     * Gets multiple instances based on the types specified.
     *
     * If the type is not specified, the value will not be present.
     *
     * @param types Subtypes of I
     * @return Set of instances, if the instance mappings have been set up
     */
    public Collection<I> getMultiple(Set<Class<? extends I>> types) {
        return types.stream()
            .map(this::get)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

    /**
     *
     * @return All of the underlying instances present in the container
     */
    public Collection<I> values() {
        return map.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeterogeneousTypeContainer<?> that = (HeterogeneousTypeContainer<?>) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return "HeterogeneousTypeContainer{" +
            "map=" + map +
            '}';
    }
}
