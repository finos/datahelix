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
package com.scottlogic.deg.common.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

public class NotConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursivelySameLevel() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate().negate().negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsInSetConstraint(field1, Collections.singleton("abc"), rules()).negate();
        Constraint constraint2 = new IsInSetConstraint(field2, Collections.singleton("abcd"), rules()).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    private static Set<RuleInformation> rules(){
        return Collections.singleton(new RuleInformation());
    }
}
