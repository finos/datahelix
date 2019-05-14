package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalysisResult;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.Comparator;

public final class HierarchicalDependencyFixFieldStrategy extends ProfileBasedFixFieldStrategy {

    private final SetBasedFixFieldStrategy setBasedFixFieldStrategy;
    private final FieldDependencyAnalyser analyser;
    private final Profile profile;

    public HierarchicalDependencyFixFieldStrategy(Profile profile, FieldDependencyAnalyser analyser, DecisionTree tree) {
        this.analyser = analyser;
        this.profile = profile;
        this.setBasedFixFieldStrategy = new SetBasedFixFieldStrategy(tree);
    }

    Comparator<Field> getFieldOrderingStrategy() {
        FieldDependencyAnalysisResult result = this.analyser.analyse(profile);
        Comparator<Field> firstComparison = Comparator.comparingInt(field -> result.getDependenciesOf(field).size());
        Comparator<Field> secondComparison = Comparator.comparingInt((Field field) -> result.getDependentsOf(field).size()).reversed();
        return firstComparison.thenComparing(secondComparison)
            .thenComparing(setBasedFixFieldStrategy.getFieldOrderingStrategy())
            .thenComparing(field -> field.name);
    }

}
