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

package com.scottlogic.deg.generator.decisiontree.testutils;

import java.util.ArrayList;
import java.util.Collection;

public class AnyOrderCollectionEqualityComparer implements EqualityComparer, CollectionEqualityComparer {
    private final EqualityComparer itemEqualityComparer;

    public AnyOrderCollectionEqualityComparer() {
        itemEqualityComparer = new DefaultEqualityComparer(this);
    }

    public AnyOrderCollectionEqualityComparer(EqualityComparer itemEqualityComparer) {
        this.itemEqualityComparer = itemEqualityComparer;
    }

    @Override
    public int getHashCode(Object item) {
        Collection collection = (Collection)item;
        return collection.size();
    }

    @Override
    public boolean equals(Object x, Object y) {
        return equals((Collection) x, (Collection) y);
    }

    public boolean equals(Collection x, Collection y) {
        return collectionsContainMatchingItems(x, y) && collectionsContainMatchingItems(x, y);
    }

    public ArrayList<Object> getItemsMissingFrom(Collection x, Collection y) {
        ArrayList<Object> itemsMissing = new ArrayList<>();

        for (Object itemFromX : x) {
            Object itemFromY = findItem(itemFromX, y);

            if (itemFromY == null) {
                itemsMissing.add(itemFromX);
            }
        }

        return itemsMissing;
    }

    private boolean collectionsContainMatchingItems(Collection x, Collection y) {
        return getItemsMissingFrom(x, y).isEmpty();
    }

    private Object findItem(Object toFind, Collection collection){
        for (Object item : collection) {
            if (itemEqualityComparer.equals(toFind, item))
                return item;
        }

        return null;
    }
}
