Feature: User can specify that a numeric value is lower than, or equal to, a specified threshold

Background:
  Given the generation strategy is full
    And there is a field foo

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: Running a 'lessThanOrEqualTo' request that includes positive integer should be successful
  Given foo is less than or equal to 10
    And the generator can generate at most 10 rows
    And foo is anything but null
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | 1    |
    | 2    |
    | 3    |
    | 4    |
    | 5    |
    | 6    |
    | 7    |
    | 8    |
    | 9    |
    | 10   |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: Running a 'lessThanOrEqualTo' request that includes positive decimal should be successful
  Given foo is less than or equal to 10.0
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | 10.0 |
    | 9.9  |
    | 9.8  |
    | 9.7  |
    | 9.6  |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: Running a 'lessThanOrEqualTo' request that includes negative integer should be successful
  Given foo is less than or equal to -10
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | -10  |
    | -9   |
    | -8   |
    | -7   |
    | -6   |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: Running a 'lessThanOrEqualTo' request that includes 0 should be successful
  Given foo is less than or equal to 0
    And the generator can generate at most 5 rows
    And foo is anything but null
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | 0    |
    | -1   |
    | -2   |
    | -3   |
    | -4   |

Scenario: Running a 'lessThanOrEqualTo' request that includes a string should fail
  Given foo is less than or equal to "Zero"
    And foo is of type "integer"
  Then I am presented with an error message
    And no data is created

Scenario: Running a 'lessThanOrEqualTo' request that includes an empty string should fail
  Given foo is less than or equal to ""
    And foo is of type "integer"
  Then I am presented with an error message
    And no data is created

Scenario: Running a 'lessThanOrEqualTo' request that specifies null should be unsuccessful
  Given foo is less than or equal to null
    And foo is of type "integer"
  Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
    And no data is created

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting lessThanOrEqualToOrEqualTo should be successful
  Given foo is less than or equal to 5
    And foo is less than or equal to 5
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 5    |
    | 4    |
    | 3    |
    | 2    |
    | 1    |

Scenario: lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
  Given foo is less than or equal to 5
    And foo is anything but less than or equal to 3
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | null |
    | 5    |
    | 4    |

Scenario: not lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
  Given foo is anything but less than or equal to 3
    And foo is anything but less than or equal to 3
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 5    |
    | 6    |
    | 7    |
    | 8    |

Scenario: lessThanOrEqualTo run against a contradicting not lessThanOrEqualToOrEqualTo should only only generate null
  Given foo is less than or equal to 3
    And foo is anything but less than or equal to 3
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | null |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting granularTo should be successful
  Given foo is less than or equal to 3
    And foo is of type "decimal"
    And foo is granular to 1
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 3    |
    | 2    |
    | 1    |
    | 0    |
    | -1   |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting not granularTo should be successful
  Given foo is less than or equal to 3
    And foo is anything but granular to 0.1
    And foo is of type "decimal"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 3    |
    | 2    |
    | 1    |
    | 0    |
    | -1   |

Scenario: not lessThanOrEqualTo run against a non contradicting granularTo should be successful
  Given foo is anything but less than or equal to 3
    And the generator can generate at most 5 rows
    And foo is of type "decimal"
    And foo is granular to 1
    And foo is anything but null
    Then the following data should be generated:
    | foo  |
    | 4    |
    | 5    |
    | 6    |
    | 7    |
    | 8    |

Scenario: not lessThanOrEqualTo run against a non contradicting not granularTo should be successful
  Given foo is anything but less than or equal to 3
    And foo is anything but granular to 0.1
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 5    |
    | 6    |
    | 7    |
    | 8    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting after should be successful
  Given foo is less than or equal to 5
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting not after should be successful
  Given foo is less than or equal to 5
    And foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting afterOrAt should be successful
  Given foo is less than or equal to 5
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting not afterOrAt should be successful
  Given foo is less than or equal to 5
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting before should be successful
  Given foo is less than or equal to 5
    And foo is before 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting not before should be successful
  Given foo is less than or equal to 5
    And foo is anything but before 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting beforeOrAt should be successful
  Given foo is less than or equal to 5
    And foo is before or at 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |

@ignore #594 - Reverse order of value generation when only upper-bound operators are provided
Scenario: lessThanOrEqualTo run against a non contradicting not beforeOrAt should be successful
  Given foo is less than or equal to 5
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And the generator can generate at most 5 rows
    And foo is anything but null
  Then the following data should be generated:
    | foo  |
    | 4    |
    | 3    |
    | 2    |
    | 1    |
    | 0    |