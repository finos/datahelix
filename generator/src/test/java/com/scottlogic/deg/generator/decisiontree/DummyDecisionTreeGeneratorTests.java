package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DummyDecisionTreeGeneratorTests {
    @Test
    public void shouldReturnNull() {
        DummyDecisionTreeGenerator testObject = new DummyDecisionTreeGenerator();
        Profile profile = new Profile(new ArrayList<>(), new ArrayList<>());

        IDecisionTreeProfile testOutput = testObject.analyse(profile);

        Assert.assertThat("DummyDecisionTreeGenerator.analyse() always returns null", testOutput,
                Is.is(IsNull.nullValue()));
    }
}
