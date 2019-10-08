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

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DecisionNode implements Node {
    private final Set<ConstraintNode> options;
    private final Set<NodeMarking> nodeMarkings;

    public DecisionNode(ConstraintNode... options) {
        this(Collections.unmodifiableSet(SetUtils.setOf(options)));
    }

    public DecisionNode(Set<ConstraintNode> options) {
        this(options, Collections.emptySet());
    }

    public DecisionNode(Set<ConstraintNode> options, Set<NodeMarking> nodeMarkings) {
        this.options = Collections.unmodifiableSet(options);
        this.nodeMarkings = Collections.unmodifiableSet(nodeMarkings);
    }

    public Set<ConstraintNode> getOptions() {
        return options;
    }

    public DecisionNode setOptions(Set<ConstraintNode> options){
        return new DecisionNode(options);
    }

    @Override
    public boolean hasMarking(NodeMarking detail) {
        return this.nodeMarkings.contains(detail);
    }

    public DecisionNode markNode(NodeMarking marking) {
        Set<NodeMarking> newMarkings = FlatMappingSpliterator.flatMap(
                Stream.of(Collections.singleton(marking), this.nodeMarkings),
                Collection::stream)
            .collect(Collectors.toSet());
        return new DecisionNode(this.options, newMarkings);
    }

    @Override
    public String toString(){
        return this.options.size() >= 5
            ? String.format("Options: %d", this.options.size())
            : String.format("Options [%d]: %s",
                this.options.size(),
            this.options.stream().map(ConstraintNode::toString).collect(Collectors.joining(" OR ")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecisionNode that = (DecisionNode) o;
        return options.containsAll(that.options) && that.options.containsAll(options);
    }

    @Override
    public int hashCode() {
        List<ConstraintNode> optionsList = new ArrayList<>(options);
        return Objects.hash(optionsList);
    }
}
