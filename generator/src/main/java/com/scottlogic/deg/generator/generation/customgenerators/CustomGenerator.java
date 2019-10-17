package com.scottlogic.deg.generator.generation.customgenerators;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.stream.Stream;

public interface CustomGenerator<T> {

    /***
     * REQUIRED! used in profile reading
     * @return the name of the custom generator
     */
    String generatorName();

    /**
     * STRING for String's
     * NUMERIC for BigDecimal's
     * DATETIME for OffsetDateTime's
     * @return accepted field type
     */
    FieldType fieldType();

    /**
     * the part of the generator to be used during random generation
     *
     * Required if you want your custom generator to support random generation
     *
     * @param randomNumberGenerator the datahelix randomNumberGenerator that can be used for randomising
     *
     * @return your stream of random values
     */
    Stream<T> generateRandom(RandomNumberGenerator randomNumberGenerator);

    /**
     * the part of the generator to be used when the generator constraint is negated during random generation
     *  - this should be implemented as the values that your regular generator should not be outputting
     *
     * Required if you want your custom generator to support being negated in random mode
     * Required if you want your custom generator to support used in the IF part of IF THEN constraints
     *
     * @param randomNumberGenerator the datahelix randomNumberGenerator that can be used for randomising
     *
     * @return your stream of random values that are not what would be produced by the generator if it were not negated
     */
    Stream<T> generateNegatedRandom(RandomNumberGenerator randomNumberGenerator);

    /**
     * the part of the generator to be used during sequential generation
     *
     * Required if you want your custom generator to support sequential generation
     * Required if you want your custom generator to support unique keys generation
     *
     * @return your stream of random values
     */
    Stream<T> generateSequential();

    /**
     * the part of the generator to be used when the generator constraint is negated during sequential generation
     *
     * Required if you want your custom generator to support being negated in sequential mode
     * should not be used with unique keys generation
     *
     * @return your stream of sequential values that are not what would be produced by the generator if it were not negated
     */
    Stream<T> generateNegatedSequential();

    /**
     * The function to check whether a value from a set can be generated from this generator
     *
     * Required if you want your custom generator to support being combined with inSet and equalTo constraints
     *
     * @param value the value from the set to be tested against
     *
     * @return true if the generator would be able to produce the value
     */
    boolean setMatchingFunction(T value);
}
