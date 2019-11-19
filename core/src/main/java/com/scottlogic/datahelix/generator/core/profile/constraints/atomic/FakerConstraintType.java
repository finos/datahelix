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
package com.scottlogic.datahelix.generator.core.profile.constraints.atomic;

import com.github.javafaker.Faker;

import java.util.function.Function;

public enum FakerConstraintType {
    FIRST_NAME(faker -> faker.name().firstName()),
    LAST_NAME(faker -> faker.name().lastName()),
    NAME(faker -> faker.name().name()),
    BLOOD_GROUP(faker -> faker.name().bloodGroup()),
    USER_NAME(faker -> faker.name().username()),
    NAME_PREFIX(faker -> faker.name().prefix()),
    PHONE_NUMBER(faker -> faker.phoneNumber().phoneNumber()),
    CELL_NUMBER(faker -> faker.phoneNumber().cellPhone());

    private final Function<Faker, String> fakerFunction;

    FakerConstraintType(Function<Faker, String> fakerFunction) {
        this.fakerFunction = fakerFunction;
    }

    public Function<Faker, String> getFakerFunction() {
        return fakerFunction;
    }
}
