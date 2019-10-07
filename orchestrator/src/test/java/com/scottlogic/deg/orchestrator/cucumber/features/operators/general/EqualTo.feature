Feature: User can specify that a value is equalTo a required value

  Background:
    Given the generation strategy is full

### alone ###

  Scenario: Running an 'equalTo' of a string should return only the string
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "Test String 1"
    Then the following data should be generated:
      | foo             |
      | "Test String 1" |

  Scenario: Running an 'equalTo' of a number should return only that number
    Given there is a field foo
    And foo has type "decimal"
    And foo is equal to 0.14
    Then the following data should be generated:
      | foo  |
      | 0.14 |

  Scenario: Running an 'equalTo' of a dateTime value should return only that date
    Given there is a field foo
    And foo has type "datetime"
    And foo is equal to 2010-01-01T00:03:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2010-01-01T00:03:00.000Z |


  Scenario: Running an 'equalTo' of an empty string should return only the empty string
    Given there is a field foo
    And foo has type "string"
    And foo is equal to ""
    Then the following data should be generated:
      | foo  |
      | ""   |

  Scenario: Running an 'equalTo' of null should fail with an error message
      Given there is a field foo
      And foo has type "string"
      And foo is equal to null
      Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"

  Scenario: Running an 'equalTo' of an invalid date value should fail with an error message
    Given there is a field foo
    And foo is equal to 2010-13-40T00:00:00.000Z
    Then the profile is invalid because "Field \[foo\]: Date string '2010-13-40T00:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running an 'equalTo' request that includes an invalid time value  should fail with an error message
    Given there is a field foo
    And foo is equal to 2010-01-01T55:00:00.000Z
    Then the profile is invalid because "Field \[foo\]: Date string '2010-01-01T55:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running a not 'equalTo' should allow null
    Given there is a field foo
    And foo has type "string"
    And foo is anything but equal to "not"
    And foo is null
    Then the following data should be generated:
      | foo   |
      | null |

### EqualTo ###

  Scenario: Two equivalent 'equalTo' statements should be successful
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "a"
    And foo is equal to "a"
    Then the following data should be generated:
      | foo  |
      | "a"  |

  Scenario: A not 'equalTo' statement should have no impact on an 'equalTo' statement
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "a"
    And foo is anything but equal to "A"
    Then the following data should be generated:
      | foo  |
      | "a"  |

  Scenario: Contradictory 'equalTo' statements should emit no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "a"
    And foo is equal to "b"
    Then no data is created

### InSet ###

  Scenario: Running an 'inSet' request alongside a non-contradicting 'equalTo' constraint should return only that value
    Given there is a field foo
    And foo has type "string"
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is equal to "Test 1"
    Then the following data should be generated:
      | foo      |
      | "Test 1" |

  Scenario: Running an 'inSet' request alongside a contradicting 'equalTo' constraint should emit no data
    Given there is a field foo
    And foo has type "string"
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is equal to "Test 4"
    Then no data is created

### null ###

  Scenario: 'EqualTo' and not null should be successful
    Given there is a field foo
    And foo has type "decimal"
    And foo is equal to 15
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 15  |

  Scenario: 'EqualTo' a value and must be null should be contradictory
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "a"
    And foo is null
    Then no data is created

### ofType ###

  Scenario Outline: 'EqualTo' should combine with type <type>
    Given there is a field foo
    And foo is equal to <value>
    And foo has type <type>
    Then the following data should be generated:
      | foo     |
      | <value> |
    Examples:
      | type | value |
      | "integer"  | 1                        |
      | "string"   | "test"                   |
      | "datetime" | 2000-01-01T00:00:00.001Z |
      | "decimal"  | 1.1                      |

### constraints ###

  Scenario Outline: 'EqualTo' alongside a non-contradicting <operator> should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo has type <type>
    And foo is <operator>
    Then the following data should be generated:
      | foo      |
      | <value> |
    Examples:
      | operator | value | type |
      | of length 1             | "a"   | "string" |
      | longer than 1           | "ab"  | "string" |
      | shorter than 1          | ""    | "string" |
      | matching regex /[a]{3}/ | "aaa" | "string" |

      | greater than 1              | 2   | "decimal" |
      | less than 1                 | 0   | "decimal" |
      | greater than or equal to 1  | 1   | "decimal" |
      | less than or equal to 1     | 1   | "decimal" |
      | granular to 0.1             | 1.2 | "decimal" |

      | after 2018-01-01T00:00:00.000Z        | 2019-01-01T00:00:00.000Z | "datetime" |
      | before 2020-01-01T00:00:00.000Z       | 2019-01-01T00:00:00.000Z | "datetime" |
      | after or at 2019-01-01T00:00:00.000Z  | 2019-01-01T00:00:00.000Z | "datetime" |
      | before or at 2019-01-01T00:00:00.000Z | 2019-01-01T00:00:00.000Z | "datetime" |
      | granular to "seconds"                 | 2019-01-01T00:00:01.000Z | "datetime" |

### Max String Length ###

  Scenario: 'EqualTo' request including a string of the maximum length should be successful
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "I am 1000 chars long   jdny97XhjJE0ywt6mRMfYj1ECoNufcF3Dy2DStFmnLVHH5GcfLtLTXEG34LNgTxPvmAqYL6UCWiia23IqmzrooICtND1UtSbrsDOhQeVjNUjTNMsin6AO5oSOiLkpU0h4hctiKKg8IoZ05TrRyl8ZBg99S986vM737sSUxUv3yKj8lPOMH5ZjrgAn52D2LerAlBRvcQMoYP5mnuPidtCHT6RrHMJX44nHFeMJS6371dHMC9bDqjJRrMsnu1DWc7kUkttSPioKZbR1BDUn5s1WTM5brzWv9bgWvtFhjzHYdhMY0bxq1qXksGzAqaOkcbbUh6bCirz6N4nAt4I2aQccMQqCp5TjXAFGMLxbRO7uttWZI8GRWiXP2joA9aTw7K8Fk5rllWbGfgFHSlMHYmeGGRF8ig10LgkeVDdP7tVHyGr4O6nKV3TB61UJaHCRZUIoyPuce3SWeckv835iwVrKy9PIC5D42HBd3431GIyMy7sxpR4pWs7djW6UxhdnTC3q2MlX0aMXjDrLCAjybo89q7qJw4eEPfR2cwuc1xvSiC2RoVVlBprmLkKiDeCZPRZxxVn9QwzvPNnRsjx9nFenwfPIDf1C6MbQ22aYmxqcnxQky1gLLdPRWVYpgqzeztnBziahVuZZLob5EvFjgv5HmKnfg3DUrU2Em61l9nE0L6IYiz9xrZ0kmiDSB44cEOoubhJUwihD7PrM92pmCKXoWouigS6LSlCIX8OkQxaHRA0m2FYgtYV0H9rkK0kQfflvlF3zd7TvSjW1NGRxzjh5jGNfvkl9M9O5tpvieoM55uPi2fY9f8ZD2Eq0KjEHEcKtLNWnxdpuIVa7mzByWqkawwrhdjH0qF4RwXsGbTHhrNT7SFyBs4h1MdKEkUlrXgGlXXtSo104KsMv5qWIXRI221jjfwZZ7nl1XLSSOqLhDoWdvgiR0XPPwvLtPMBWiwqW86upHDMMcPAYKCnP"
    Then the following data should be generated:
      | foo                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
      | "I am 1000 chars long   jdny97XhjJE0ywt6mRMfYj1ECoNufcF3Dy2DStFmnLVHH5GcfLtLTXEG34LNgTxPvmAqYL6UCWiia23IqmzrooICtND1UtSbrsDOhQeVjNUjTNMsin6AO5oSOiLkpU0h4hctiKKg8IoZ05TrRyl8ZBg99S986vM737sSUxUv3yKj8lPOMH5ZjrgAn52D2LerAlBRvcQMoYP5mnuPidtCHT6RrHMJX44nHFeMJS6371dHMC9bDqjJRrMsnu1DWc7kUkttSPioKZbR1BDUn5s1WTM5brzWv9bgWvtFhjzHYdhMY0bxq1qXksGzAqaOkcbbUh6bCirz6N4nAt4I2aQccMQqCp5TjXAFGMLxbRO7uttWZI8GRWiXP2joA9aTw7K8Fk5rllWbGfgFHSlMHYmeGGRF8ig10LgkeVDdP7tVHyGr4O6nKV3TB61UJaHCRZUIoyPuce3SWeckv835iwVrKy9PIC5D42HBd3431GIyMy7sxpR4pWs7djW6UxhdnTC3q2MlX0aMXjDrLCAjybo89q7qJw4eEPfR2cwuc1xvSiC2RoVVlBprmLkKiDeCZPRZxxVn9QwzvPNnRsjx9nFenwfPIDf1C6MbQ22aYmxqcnxQky1gLLdPRWVYpgqzeztnBziahVuZZLob5EvFjgv5HmKnfg3DUrU2Em61l9nE0L6IYiz9xrZ0kmiDSB44cEOoubhJUwihD7PrM92pmCKXoWouigS6LSlCIX8OkQxaHRA0m2FYgtYV0H9rkK0kQfflvlF3zd7TvSjW1NGRxzjh5jGNfvkl9M9O5tpvieoM55uPi2fY9f8ZD2Eq0KjEHEcKtLNWnxdpuIVa7mzByWqkawwrhdjH0qF4RwXsGbTHhrNT7SFyBs4h1MdKEkUlrXgGlXXtSo104KsMv5qWIXRI221jjfwZZ7nl1XLSSOqLhDoWdvgiR0XPPwvLtPMBWiwqW86upHDMMcPAYKCnP" |

### Financial code types ###

  Scenario: Equal to a valid ISIN combined with an ISIN constraint should generate the equal to value
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "GB0002634946"
    And foo has type "ISIN"
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |

  Scenario: Equal to something that is not a valid ISIN because its check digit is wrong combined with an ISIN constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "GB00026349"
    And foo has type "ISIN"
    Then no data is created

  Scenario: Equal to something that is not a valid ISIN combined with an ISIN constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "aa"
    And foo has type "ISIN"
    Then no data is created

  Scenario: Not equal to something that is not a valid SEDOL combined with a SEDOL constraint should generate SEDOLs
    Given there is a field foo
    And foo has type "string"
    And foo is anything but equal to "a"
    And foo has type "SEDOL"
    And foo is in set:
      | "a"       |
      | "0263494" |
      | "0263497" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |

  Scenario: Equal to something that is not a valid SEDOL because its check digit is wrong combined with a SEDOL constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "0263497"
    And foo has type "SEDOL"
    Then no data is created

  Scenario: Equal to something that is not a valid SEDOL combined with a SEDOL constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "aa"
    And foo has type "SEDOL"
    Then no data is created

  Scenario: Not equal to something that is not a valid CUSIP combined with a CUSIP constraint should generate valid CUSIPs
    Given there is a field foo
    And foo has type "string"
    And foo is anything but equal to "a"
    And foo has type "CUSIP"
    And foo is in set:
      | "a"         |
      | "38259P508" |
      | "38259P502" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |

  Scenario: Equal to something that is not a valid CUSIP because its check digit is wrong combined with a CUSIP constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "38259P502"
    And foo has type "CUSIP"
    Then no data is created

  Scenario: Equal to something that is not a valid CUSIP combined with a CUSIP constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "aa"
    And foo has type "CUSIP"
    Then no data is created

  Scenario: Equal to a valid RIC combined with an RIC constraint should generate the equal to value
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "R.IC"
    And foo has type "RIC"
    Then the following data should be generated:
      | foo            |
      | "R.IC" |

  Scenario: Equal to not a RIC combined with an RIC constraint should generate no data
    Given there is a field foo
    And foo has type "string"
    And foo is equal to "NOTRIC"
    And foo has type "RIC"
    Then no data is created

