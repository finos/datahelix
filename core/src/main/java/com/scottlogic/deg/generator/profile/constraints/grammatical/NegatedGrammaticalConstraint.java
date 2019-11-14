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

package com.scottlogic.deg.generator.profile.constraints.grammatical;

import java.util.Objects;

public class NegatedGrammaticalConstraint implements GrammaticalConstraint {
    private final GrammaticalConstraint negatedConstraint;

    NegatedGrammaticalConstraint(GrammaticalConstraint negatedConstraint) {
        if (negatedConstraint instanceof NegatedGrammaticalConstraint)
            throw new IllegalArgumentException("nested NegatedGrammatical constraint not allowed");
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public GrammaticalConstraint negate() {
        return this.negatedConstraint;
    }

    private GrammaticalConstraint getBaseConstraint(){
        return negatedConstraint;
    }

    public String toString(){
        return String.format(
                "NOT(%s)",
                negatedConstraint);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NegatedGrammaticalConstraint otherConstraint = (NegatedGrammaticalConstraint) o;
        return Objects.equals(getBaseConstraint(), otherConstraint.getBaseConstraint());
    }

    @Override
    public int hashCode(){
        return Objects.hash("NOT", negatedConstraint.hashCode());
    }

    public GrammaticalConstraint getNegatedConstraint() {
        return negatedConstraint;
    }
}
