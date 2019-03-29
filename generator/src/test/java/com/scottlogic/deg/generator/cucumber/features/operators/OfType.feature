Feature: User can specify that a field is of a specific type (string, integer, decimal, or datetime).

Background:
     Given the generation strategy is full

Scenario: Running an 'ofType' = integer request that includes a number value (not a string) should be successful
     Given there is a field foo
       And foo is equal to 1
       And foo is of type "integer"
     Then the following data should be generated:
       | foo  |
       | null |
       | 1    |

Scenario: Running an 'ofType' = decimal request that includes a decimal number value should be successful
     Given there is a field foo
       And foo is equal to 0.66
       And foo is of type "decimal"
     Then the following data should be generated:
       | foo  |
       | null |
       | 0.66 |

Scenario: Running an 'ofType' = decimal request that includes a negative number value should be successful
     Given there is a field foo
       And foo is equal to -99.4
       And foo is of type "decimal"
     Then the following data should be generated:
       | foo   |
       | null  |
       | -99.4 |

Scenario: Running an 'ofType' = integer request that includes a negative number value should be successful
  Given there is a field foo
    And foo is equal to -99
    And foo is of type "integer"
  Then the following data should be generated:
    | foo   |
    | null  |
    | -99   |

Scenario: Running an 'ofType' = integer request that includes the number zero should be successful
     Given there is a field foo
       And foo is equal to 0
       And foo is of type "integer"
     Then the following data should be generated:
       | foo   |
       | null  |
       | 0     |

Scenario: Running an 'ofType' = decimal request that includes the number zero should be successful
  Given there is a field foo
    And foo is equal to 0
    And foo is of type "decimal"
  Then the following data should be generated:
    | foo   |
    | null  |
    | 0     |

Scenario: Running an 'ofType' = datetime request that includes a date value (not a string) should be successful
     Given there is a field foo
       And foo is equal to 2010-01-01T00:00:00.000Z
       And foo is of type "datetime"
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 2010-01-01T00:00:00.000Z |

Scenario: Running an 'ofType' = datetime request that includes a date value (leap year) should be successful
     Given there is a field foo
       And foo is equal to 2020-02-29T09:15:00.000Z
       And foo is of type "datetime"
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 2020-02-29T09:15:00.000Z |

Scenario: Running an 'ofType' = datetime request that includes a date value (system max future dates) should be successful
     Given there is a field foo
        And foo is equal to 9999-12-31T23:59:59.999Z
        And foo is of type "datetime"
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 9999-12-31T23:59:59.999Z |

Scenario: Running an 'ofType' = datetime request that includes an invalid date value should fail with an error message
     Given there is a field foo
       And foo is equal to 2010-13-40T00:00:00.000Z
       And foo is of type "datetime"
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'ofType' = datetime request that includes an invalid time value should fail with an error message
     Given there is a field foo
       And foo is equal to 2010-01-01T75:00:00.000Z
       And foo is of type "datetime"
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'ofType' = string request that includes a null entry ("") characters should be successful
     Given there is a field foo
       And foo is equal to ""
       And foo is of type "string"
     Then the following data should be generated:
       | foo  |
       | null |
       | ""   |

Scenario: Running a 'ofType' request that specifies null should be unsuccessful
    Given foo is of type null
    Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
      And no data is created

Scenario: Running an 'ofType' = numeric request should throw an error
  Given there is a field foo
    And foo is of type "numeric"
  Then the profile is invalid because "Numeric type is no longer supported. Please use one of "decimal" or "integer""