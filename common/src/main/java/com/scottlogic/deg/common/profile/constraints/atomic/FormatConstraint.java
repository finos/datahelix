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
package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Set;

public class FormatConstraint implements AtomicConstraint {

    public final Field field;
    public final String format;
    private final Set<RuleInformation> rules;

    public FormatConstraint(Field field, String format, Set<RuleInformation> rules) {
        this.field = field;
        this.format = format;
        this.rules = rules;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s has format '%s'", field.name, format);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new FormatConstraint(this.field, this.format, rules);
    }
}
