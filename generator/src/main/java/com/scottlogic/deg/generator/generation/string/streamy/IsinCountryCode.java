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
package com.scottlogic.deg.generator.generation.string.streamy;


import static com.scottlogic.deg.generator.generation.string.streamy.ChecksumStringGeneratorFactory.createCusipGenerator;
import static com.scottlogic.deg.generator.generation.string.streamy.ChecksumStringGeneratorFactory.createSedolGenerator;

public enum IsinCountryCode {
    GB(prefix("GB00", createSedolGenerator())),
    US(prefix("US", createCusipGenerator())),
    DN;

    String GENERIC_NSIN_REGEX = "[A-Z0-9]{9}";
    private final StreamStringGenerator checksumlessStringGenerator;

    IsinCountryCode(){
        checksumlessStringGenerator = prefix(this.name(),
            new RegexStreamStringGenerator(GENERIC_NSIN_REGEX));
    }

    IsinCountryCode(StreamStringGenerator streamStringGenerator){
        this.checksumlessStringGenerator = streamStringGenerator;
    }

    static StreamStringGenerator prefix(String prefix, StreamStringGenerator inner){
        return new PrefixingStreamStringGenerator(prefix, inner);
    }

    public StreamStringGenerator getChecksumlessStringGenerator() {
        return checksumlessStringGenerator;
    }
}
