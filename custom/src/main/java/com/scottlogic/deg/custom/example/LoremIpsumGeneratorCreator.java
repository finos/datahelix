package com.scottlogic.deg.custom.example;

import com.scottlogic.deg.custom.CustomGenerator;
import com.scottlogic.deg.custom.builder.CustomGeneratorBuilder;

import java.util.stream.Stream;

public class LoremIpsumGeneratorCreator {

    public static CustomGenerator create(){
        RandomLoremIpsum randomLoremIpsum = new RandomLoremIpsum();

        return CustomGeneratorBuilder
            .createStringGenerator("lorem ipsum")
            .withRandomGenerator(() -> randomLoremIpsum.generateString())
            .withSequentialGenerator(() -> Stream.generate(() -> loremIpsumText))
            .build();
    }

    private static String loremIpsumText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
}
