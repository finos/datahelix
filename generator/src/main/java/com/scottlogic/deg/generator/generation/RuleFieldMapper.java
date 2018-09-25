package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeProfile;
import com.scottlogic.deg.generator.decisiontree.RuleDecisionTree;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSource;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.utils.HardLimitingIterable;
import com.scottlogic.deg.generator.utils.ProjectingIterable;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Given a rule or list of rules it finds which rules act on which fields and returns a map of rules to fields
 */
public class RuleFieldMapper {
    public List<Field> mapRuleToField(RuleDecisionTree rule) {
        return null;
    }

    public Map<RuleDecisionTree, Field> mapRulesToFields(DecisionTreeProfile profile){
        return null;
    }
}
