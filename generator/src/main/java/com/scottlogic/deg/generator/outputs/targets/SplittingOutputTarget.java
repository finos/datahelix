package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.formats.SplittingDataSetWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** Encapsulates a collection of other targets and delegates onto them */
public class SplittingOutputTarget implements SingleDatasetOutputTarget {
    private final Collection<SingleDatasetOutputTarget> subTargets;

    public SplittingOutputTarget(SingleDatasetOutputTarget ...subTargets) {
        this.subTargets = Arrays.asList(subTargets);
    }

    @Override
    public DataSetWriter openWriter(ProfileFields fields) throws IOException {
        List<DataSetWriter> list = new ArrayList<>();

        for (SingleDatasetOutputTarget subTarget : subTargets) {
            list.add(subTarget.openWriter(fields)); // small chance of unclosed writer here
        }

        return new SplittingDataSetWriter(list);
    }
}
