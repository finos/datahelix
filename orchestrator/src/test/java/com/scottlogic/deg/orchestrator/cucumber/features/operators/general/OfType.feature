Feature: User can specify that a field is of a specific type (string, integer, decimal, or datetime).

  Background:
    Given the generation strategy is full

  Scenario: Running an 'ofType' = integer request that includes a number value (not a string) should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | 1    |
  Scenario: Running an 'ofType' = integer request that includes a number value (as a string) should be successful
    Given there is a field foo
    And foo is equal to "1"
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | 1    |

  Scenario: Running an 'ofType' = decimal request that includes a decimal number value should be successful
    Given there is a field foo
    And foo is equal to 0.66
    And foo has type "decimal"
    Then the following data should be generated:
      | foo  |
      | 0.66 |

  Scenario: Running an 'ofType' = decimal request that includes a negative number value should be successful
    Given there is a field foo
    And foo is equal to -99.4
    And foo has type "decimal"
    Then the following data should be generated:
      | foo   |
      | -99.4 |

  Scenario: Running an 'ofType' = integer request that includes a negative number value should be successful
    Given there is a field foo
    And foo is equal to -99
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | -99  |

  Scenario: Running an 'ofType' = integer request that includes the number zero should be successful
    Given there is a field foo
    And foo is equal to 0
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | 0    |

  Scenario: Running an 'ofType' = decimal request that includes the number zero should be successful
    Given there is a field foo
    And foo is equal to 0
    And foo has type "decimal"
    Then the following data should be generated:
      | foo  |
      | 0    |

  Scenario: Running an 'ofType' = datetime request that includes a date value (not a string) should be successful
    Given there is a field foo
    And foo is equal to 2010-01-01T00:00:00.000Z
    And foo has type "datetime"
    Then the following data should be generated:
      | foo                      |
      | 2010-01-01T00:00:00.000Z |

  Scenario: Running an 'ofType' = datetime request that includes a date value (leap year) should be successful
    Given there is a field foo
    And foo is equal to 2020-02-29T09:15:00.000Z
    And foo has type "datetime"
    Then the following data should be generated:
      | foo                      |
      | 2020-02-29T09:15:00.000Z |

  Scenario: Running an 'ofType' = datetime request that includes a date value (system max future dates) should be successful
    Given there is a field foo
    And foo is equal to 9999-12-31T23:59:59.999Z
    And foo has type "datetime"
    Then the following data should be generated:
      | foo                      |
      | 9999-12-31T23:59:59.999Z |

  Scenario: Running an 'ofType' = datetime request that includes an invalid date value should fail with an error message
    Given there is a field foo
    And foo is equal to 2010-13-40T00:00:00.000Z
    And foo has type "datetime"
    Then the profile is invalid because "Field \[foo\]: Date string '2010-13-40T00:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running an 'ofType' = datetime request that includes an invalid time value should fail with an error message
    Given there is a field foo
    And foo is equal to 2010-01-01T75:00:00.000Z
    And foo has type "datetime"
    Then the profile is invalid because "Field \[foo\]: Date string '2010-01-01T75:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

    @ignore #pending development of 1381 - Add date time and fields
  Scenario: Running an 'ofType' = date request should be successful
    Given there is a field foo
    And foo is equal to 2010-01-01
    And foo has type "date"
    Then the following data should be generated:
      | foo        |
      | 2010-01-01 |

   @ignore #pending development of 1381 - Add date time and fields
  Scenario: Running an 'ofType' = time request should be successful
    Given there is a field foo
    And foo is equal to T09:15:00.000Z
    And foo has type "time"
    Then the following data should be generated:
      | foo            |
      | T09:15:00.000Z |

  Scenario: Running an 'ofType' = string request without other constraints should generate strings up to implicit maximum length
    Given there is a field foo
    And foo has type "string"
    And the generation strategy is random
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 1 and 1000 inclusively

  Scenario: Running a 'ofType' request that specifies null should be unsuccessful
    Given there is a field foo
    Then the profile is invalid because "Field \[foo\]: is not typed; add its type to the field definition"
    And no data is created

