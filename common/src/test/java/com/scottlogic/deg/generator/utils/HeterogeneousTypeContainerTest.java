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
package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.common.util.HeterogeneousTypeContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeterogeneousTypeContainerTest {

    @Test
    void container_shouldReturnArbitraryTypes_ifGeneric() {
        HeterogeneousTypeContainer<Object> container = new HeterogeneousTypeContainer<>();
        String string = "a string";
        Integer integer = 3;
        container = container.put(String.class, string);
        container = container.put(Integer.class, integer);

        assertEquals(string, container.get(String.class).get());
        assertEquals(integer, container.get(Integer.class).get());
    }

    @Test
    void container_shouldReturnInterfaceTypes_ifSpecific() {
        HeterogeneousTypeContainer<Number> numbers = new HeterogeneousTypeContainer<>();
        Integer integerNumber = 2;
        Float floatNumber = 3.0f;

        numbers = numbers.put(Integer.class, integerNumber).put(Float.class, floatNumber);

        assertEquals(integerNumber, numbers.get(Integer.class).get());
        assertEquals(floatNumber, numbers.get(Float.class).get());

    }

}