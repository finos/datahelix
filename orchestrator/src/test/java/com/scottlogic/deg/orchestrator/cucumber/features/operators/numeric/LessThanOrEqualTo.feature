Feature: User can specify that a numeric value is lower than, or equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: Running a 'lessThanOrEqualTo' request that includes a string should fail
    Given foo is less than or equal to "Zero"
    And foo has type "integer"
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be an Number but was a String with value `Zero`"
    And no data is created

  Scenario: Running a 'lessThanOrEqualTo' request that includes an empty string should fail
    Given foo is less than or equal to ""
    And foo has type "integer"
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be an Number but was a String with value ``"
    And no data is created

  Scenario: Running a 'lessThanOrEqualTo' request that specifies null should be unsuccessful
    Given foo is less than or equal to null
    And foo has type "integer"
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"
    And no data is created

  Scenario: lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
    Given foo is less than or equal to 5
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 5    |
      | 4    |

  Scenario: not lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
    Given foo is anything but less than or equal to 3
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |

  Scenario: lessThanOrEqualTo run against a contradicting not lessThanOrEqualToOrEqualTo should only only generate null
    Given foo is less than or equal to 3
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: not lessThanOrEqualTo run against a non contradicting granularTo should be successful
    Given foo is anything but less than or equal to 3
    And the generator can generate at most 5 rows
    And foo has type "decimal"
    And foo is granular to 1
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |

