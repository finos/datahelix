package com.scottlogic.deg.custom;

import com.scottlogic.deg.custom.builder.CustomGeneratorBuilder;
import com.scottlogic.deg.custom.example.LoremIpsumGeneratorCreator;
import com.scottlogic.deg.custom.example.RandomLoremIpsum;

import java.util.Arrays;
import java.util.List;

public class CustomGeneratorList {
    public List<CustomGenerator> get() {
        return Arrays.asList(
            LoremIpsumGeneratorCreator.create()
        );
    }
}
