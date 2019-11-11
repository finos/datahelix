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
