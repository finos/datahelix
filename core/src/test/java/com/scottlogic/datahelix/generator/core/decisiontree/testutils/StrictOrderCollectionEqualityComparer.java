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

package com.scottlogic.datahelix.generator.core.decisiontree.testutils;

import java.util.Collection;
import java.util.Iterator;

public class StrictOrderCollectionEqualityComparer implements EqualityComparer {
    private final EqualityComparer itemEqualityComparer;

    public StrictOrderCollectionEqualityComparer(EqualityComparer itemEqualityComparer){
        this.itemEqualityComparer = itemEqualityComparer;
    }

    public static boolean isCollection(Object item){
        return item instanceof Collection;
    }

    @Override
    public int getHashCode(Object item) {
        Collection collection = (Collection)item;
        return collection.size();
    }

    @Override
    public boolean equals(Object x, Object y) {
        Iterator iterator1 = ((Collection)x).iterator();
        Iterator iterator2 = ((Collection)y).iterator();

        if (iterator1.hasNext() != iterator2.hasNext())
            return false;
        if (!iterator1.hasNext() && !iterator2.hasNext())
            return true; //empty collections are equal, exit early

        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false; //second collection doesn't have an item
            }

            Object item1 = iterator1.next();
            Object item2 = iterator2.next();

            if (!itemEqualityComparer.equals(item1, item2)) {
                return false; //items aren't equal
            }
        }

        return !iterator2.hasNext(); //no mismatches; collections are equal
    }
}

