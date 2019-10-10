Feature: User can specify that a numeric value is higher than, or equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: Running a 'greaterThanOrEqualTo' request that includes a positive integer should be successful
    Given foo is greater than or equal to 0
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 0   |
      | 1   |
      | 2   |
      | 3   |
      | 4   |

  Scenario: Running a 'greaterThanOrEqualTo' request that includes positive decimal should be successful
    Given foo is greater than or equal to 0.0
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "decimal"
    Then the following data should be generated:
      | foo                    |
      | 0                      |
      | 0.00000000000000000001 |
      | 0.00000000000000000002 |
      | 0.00000000000000000003 |
      | 0.00000000000000000004 |


  Scenario: Running a 'greaterThanOrEqualTo' request that includes a negative integer should be successful
    Given foo is greater than or equal to -10
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | -10 |
      | -9  |
      | -8  |
      | -7  |
      | -6  |

  Scenario: Running a 'greaterThanOrEqualTo' request that includes 0 should be successful
    Given foo is greater than or equal to 0
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 0   |
      | 1   |
      | 2   |
      | 3   |
      | 4   |

  Scenario: greaterThanOrEqualTo run against a non contradicting greaterThanOrEqualTo should be successful
    Given foo is greater than or equal to 5
    And foo is greater than or equal to 5
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
      | 9   |

  Scenario: greaterThanOrEqualTo run against a non contradicting not greaterThanOrEqualTo should be successful
    Given foo is greater than or equal to 5
    And foo is anything but greater than or equal to 10
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 5    |
      | 6    |
      | 7    |
      | 8    |
      | 9    |

  Scenario: greaterThanOrEqualTo run against a contradicting not greaterThanOrEqualTo should only only generate null
    Given foo is greater than or equal to 5
    And foo is anything but greater than or equal to 5
    And the generator can generate at most 5 rows
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: greaterThanOrEqualTo run against a non contradicting lessThan should be successful
    Given foo is greater than or equal to 5
    And foo is less than 10
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 5    |
      | 6    |
      | 7    |
      | 8    |
      | 9    |

  Scenario: greaterThanOrEqualTo run against a non contradicting not lessThan should be successful
    Given foo is greater than or equal to 5
    And foo is anything but less than 10
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 10  |
      | 11  |
      | 12  |
      | 13  |
      | 14  |

  Scenario: not greaterThanOrEqualTo run against a non contradicting not lessThan should be successful
    Given foo is anything but greater than or equal to 10
    And foo is anything but less than 5
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
      | 9   |

  Scenario: greaterThanOrEqualTo run against a contradicting lessThan should only only generate null
    Given foo is greater than or equal to 10
    And foo is less than 10
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: greaterThanOrEqualTo run against a contradicting lessThan should only only generate null
    Given foo is anything but greater than or equal to 10
    And foo is anything but less than 10
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: greaterThanOrEqualTo run against a non contradicting lessThanOrEqualTo should be successful
    Given foo is greater than or equal to 5
    And foo is less than or equal to 10
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 5    |
      | 6    |
      | 7    |
      | 8    |
      | 9    |
      | 10   |

  Scenario: greaterThanOrEqualTo run against a non contradicting not lessThanOrEqualTo should be successful
    Given foo is greater than or equal to 5
    And foo is anything but less than or equal to 5
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 6   |
      | 7   |
      | 8   |
      | 9   |
      | 10  |

  Scenario: not greaterThanOrEqualTo run against a non contradicting not lessThanOrEqualTo should be successful
    Given foo is anything but greater than or equal to 10
    And foo is anything but less than or equal to 5
    And the generator can generate at most 5 rows
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 9    |
      | 8    |
      | 7    |
      | 6    |

  Scenario: greaterThanOrEqualTo run against a contradicting lessThanOrEqualTo should only only generate null
    Given foo is greater than or equal to 6
    And foo is less than or equal to 5
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: not greaterThanOrEqualTo run against a contradicting not lessThanOrEqualTo should only only generate null
    Given foo is anything but greater than or equal to 5
    And foo is anything but less than or equal to 6
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: greaterThanOrEqualTo run against a non contradicting granularTo should be successful
    Given foo is greater than or equal to 5
    And foo is granular to 1
    And foo has type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 9   |
      | 8   |
      | 7   |
      | 6   |
      | 5   |
