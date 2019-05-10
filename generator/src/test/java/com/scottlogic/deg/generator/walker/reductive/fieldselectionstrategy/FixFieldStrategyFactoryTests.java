package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collections;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Tests the behaviour of the FixFieldStrategy class
 */
public class FixFieldStrategyFactoryTests {

    private FixFieldStrategyFactory target;
    private FieldDependencyAnalyser analyser;
    private Profile inputProfile;
    private DecisionTree inputTree;

    @BeforeEach
    public void setUp() {
        analyser = new FieldDependencyAnalyser();

        ProfileFields fields = new ProfileFields(Collections.singletonList(new Field("foo")));
        inputProfile = new Profile(fields, null);

        ConstraintNode node = new TreeConstraintNode(Collections.EMPTY_SET, Collections.EMPTY_SET);
        inputTree = new DecisionTree(node, fields, "Tree 1");

        target = new FixFieldStrategyFactory(analyser);
    }


    /**
     * Tests that the getWalkerStrategy with a config for a reductive walker returns a Hierarchical dependency fix field
     * strategy.
     */
    @Test
    public void getWalkerStrategy_withReductiveWalkerConfig_returnsHierarchicalStrategy() {
        // Arrange
        GenerationConfigSource configSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        GenerationConfig inputConfig = new GenerationConfig(configSource);

        //Act
        FixFieldStrategy actualStrategy = target.getWalkerStrategy(inputProfile, inputTree, inputConfig);

        //Assert
        HierarchicalDependencyFixFieldStrategy expectedStrategy = new HierarchicalDependencyFixFieldStrategy(
            inputProfile,
            analyser,
            inputTree
        );

        Assert.assertThat(actualStrategy, instanceOf(HierarchicalDependencyFixFieldStrategy.class));
        Assert.assertThat(actualStrategy, sameBeanAs(expectedStrategy));
    }

    /**
     * Tests that the getWalkerStrategy with a config for a walker other than the reductive walker returns a null.
     */
    @ParameterizedTest
    @EnumSource(
        value = GenerationConfig.TreeWalkerType.class,
        names = {"CARTESIAN_PRODUCT"}
    )
    public void getWalkerStrategy_withNonReductiveWalkerConfig_returnsNull(GenerationConfig.TreeWalkerType walkerType) {
        // Arrange
        GenerationConfigSource configSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
            walkerType,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        GenerationConfig inputConfig = new GenerationConfig(configSource);

        //Act
        FixFieldStrategy actualStrategy = target.getWalkerStrategy(inputProfile, inputTree, inputConfig);

        //Assert
        Assert.assertThat(actualStrategy, nullValue());
    }
}