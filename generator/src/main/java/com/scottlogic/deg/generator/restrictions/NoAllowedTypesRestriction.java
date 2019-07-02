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
package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

import java.util.*;

public class NoAllowedTypesRestriction implements TypeRestrictions {
    public boolean isTypeAllowed(IsOfTypeConstraint.Types type) {
        return false;
    }

    public String toString() {
        return "No types allowed";
    }

    public TypeRestrictions intersect(TypeRestrictions other) {
        return this;
    }

    public TypeRestrictions except(IsOfTypeConstraint.Types... types) {
        return new DataTypeRestrictions(Arrays.asList(types));
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return Collections.emptySet();
    }

    public int hashCode(){
        return this.getClass().hashCode();
    }

    public boolean equals(Object obj){
        return obj instanceof NoAllowedTypesRestriction;
    }
}
