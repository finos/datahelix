package com.scottlogic.deg.custom.example;

import java.util.Random;

public class RandomLoremIpsum {

    private Random random = new Random();

    public String generateString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Lorem ipsum");

        int length = random.nextInt(950);

        while (stringBuilder.length() < length){
            stringBuilder.append(getRandomPunctuation());
            stringBuilder.append(getRandomWord());
        }

        return stringBuilder.toString();
    }

    private String getRandomWord() {
        return "lorem ipsum";
    }

    private String getRandomPunctuation() {
        int i = random.nextInt(20);
        switch (i) {
            case 1:
            case 2:
                return ", ";
            case 3:
                return ". ";
            default:
                return " ";
        }
    }
}
