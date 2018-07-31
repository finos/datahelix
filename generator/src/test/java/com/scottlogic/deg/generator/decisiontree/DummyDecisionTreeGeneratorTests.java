package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DummyDecisionTreeGeneratorTests {
    @Test
    public void shouldReturnNull() {
        DummyDecisionTreeGenerator testObject = new DummyDecisionTreeGenerator();
        Profile profile = new Profile(new ArrayList<>(), new ArrayList<>());

        IDecisionTreeProfile testOutput = testObject.analyse(profile);

        Assert.assertNull(testOutput);
    }
}
