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

import com.scottlogic.deg.common.profile.ProfileFields;

import java.util.Collection;
import java.util.stream.Collectors;

public class ProfileFieldComparer implements EqualityComparer {
    private final EqualityComparer profileFieldsEqualityComparer;
    private final CollectionEqualityComparer profileFieldsCollectionEqualityComparer;
    private final TreeComparisonContext context;

    public ProfileFieldComparer(TreeComparisonContext context,
                                EqualityComparer profileFieldsEqualityComparer,
                                CollectionEqualityComparer profileFieldsCollectionEqualityComparer) {
        this.context = context;
        this.profileFieldsEqualityComparer = profileFieldsEqualityComparer;
        this.profileFieldsCollectionEqualityComparer = profileFieldsCollectionEqualityComparer;
    }

    @Override
    public int getHashCode(Object item) {
        return item.hashCode();
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((ProfileFields)item1, (ProfileFields)item2);
    }

    private boolean equals(ProfileFields firstProfileFields, ProfileFields secondProfileFields) {
        Collection firstProfileFieldsCollection = firstProfileFields.stream().collect(Collectors.toList());
        Collection secondProfileFieldsCollection = secondProfileFields.stream().collect(Collectors.toList());

        boolean equals = profileFieldsEqualityComparer.equals(firstProfileFieldsCollection, secondProfileFieldsCollection);
        if (!equals) {
            context.reportDifferences(
                profileFieldsCollectionEqualityComparer.getItemsMissingFrom(
                    firstProfileFieldsCollection,
                    secondProfileFieldsCollection
                ),
                profileFieldsCollectionEqualityComparer.getItemsMissingFrom(
                    secondProfileFieldsCollection,
                    firstProfileFieldsCollection
                ),
                TreeComparisonContext.TreeElementType.FIELDS
            );
        }

        return equals;
    }
}
