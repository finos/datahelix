Feature: User can specify that a numeric value is lower than, but not equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a nullable field foo
    And foo has type "decimal"

  Scenario: lessThan run against a non contradicting not lessThan should be successful (lessThan 5 AND not lessThan 1)
    Given foo is less than 5
    And foo is anything but less than 1
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |
      | 4   |

  Scenario: not lessThan run against a non contradicting lessThan should be successful (not lessThan 2 AND lessThan 5)
    Given foo is anything but less than 2
    And foo is less than 5
    And foo has type "integer"
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 2   |
      | 3   |
      | 4   |

  Scenario: not lessThan run against a non contradicting not lessThan should be successful (not lessThan 5 AND not lessThan 5)
    Given foo is anything but less than 5
    And foo is anything but less than 5
    And foo has type "integer"
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
      | 9   |

  Scenario: lessThan run against a contradicting not lessThan should only only generate string, datetime and null (lessThan 2 AND not lessThan 2)
    Given foo is less than 2
    And foo has type "integer"
    And foo is anything but less than 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: lessThan run against a non contradicting not lessThanOrEqualTo should be successful (lessThan 10 AND not lessThanOrEqualTo 2)
    Given foo is less than 10
    And foo is anything but less than or equal to 2
    And foo has type "integer"
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 3   |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
      | 9   |

  Scenario: not lessThan run against a non contradicting lessThanOrEqualTo should be successful (not lessThan 2 AND lessThanOrEqualTo 10)
    Given foo is anything but less than 2
    And foo is less than or equal to 10
    And foo has type "integer"
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 2   |
      | 3   |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
      | 9   |
      | 10  |

  Scenario: not lessThan run against a non contradicting not lessThanOrEqualTo should be successful (not lessThan 3 AND not lessThanOrEqualTo 4)
    Given foo is anything but less than 3
    And foo is anything but less than or equal to 4
    And foo has type "integer"
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
      | 9   |

  Scenario: not lessThan run against a contradicting not lessThanOrEqualTo should only only generate string, datetime and null (lessThan 2 AND not lessThanOrEqualTo 3)
    Given foo is less than 2
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: not lessThan run against a non contradicting granularTo should be successful (not lessThan 4 AND granularTo 1)
    Given foo is anything but less than 4
    And foo is granular to 1
    And foo has type "decimal"
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
