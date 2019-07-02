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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnyTypeRestriction implements TypeRestrictions {
    private final static Set<IsOfTypeConstraint.Types> allTypes = new HashSet<>(Arrays.asList(IsOfTypeConstraint.Types.values()));

    public boolean isTypeAllowed(IsOfTypeConstraint.Types type) {
        return true;
    }

    public String toString() {
        return "Any type";
    }

    public TypeRestrictions intersect(TypeRestrictions other) {
        return other;
    }


    public TypeRestrictions except(IsOfTypeConstraint.Types... types) {
        if (types.length == 0)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(allTypes);
        allowedTypes.removeAll(Arrays.asList(types));

        return new DataTypeRestrictions(allowedTypes);
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return allTypes;
    }

    public int hashCode(){
        return this.getClass().hashCode();
    }

    public boolean equals(Object obj){
        return obj instanceof AnyTypeRestriction;
    }
}
