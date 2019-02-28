Feature: User can specify that a numeric value is lower than, but not equal to, a specified threshold

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "numeric"

# alone
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'lessThan' request that specifies an integer should be successful
     Given foo is less than 1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'lessThan' request that specifies an integer with trailing zeros should be successful
    Given foo is less than 100
       And foo is anything but null
      And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo         |
      | -2147482624 |
      | -2147482623 |
      | -2147482622 |
      | -2147482621 |
      | -2147482620 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'lessThan' request that specifies a decimal should be successful
     Given foo is less than 100.1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo           |
       | -2147482624.0 |
       | -2147482623.9 |
       | -2147482623.8 |
       | -2147482623.7 |
       | -2147482623.6 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'lessThan' request that specifies a decimal with trailing zeros should be successful
    Given foo is less than 100.0
       And foo is anything but null
      And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo           |
      | -2147482624.0 |
      | -2147482623.9 |
      | -2147482623.8 |
      | -2147482623.7 |
      | -2147482623.6 |

Scenario: Running a 'lessThan' request that specifies a string should be unsuccessful
    Given foo is less than "bar"
    Then the profile is invalid
      And no data is created

Scenario: Running a 'lessThan' request that specifies an empty string should be unsuccessful
    Given foo is less than ""
    Then the profile is invalid
      And no data is created

Scenario: Running a 'lessThan' request that specifies null should be unsuccessful
    Given foo is less than null
    Then the profile is invalid
      And no data is created

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'lessThan' request that includes a negative value should be successful
     Given foo is less than 0
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | -1  |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'lessThan' request that includes the value zero should be successful
     Given foo is less than 1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 0   |

#lessThan
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting lessThan should be successful (lessThan 2 AND lessThan 5)
     Given foo is less than 2
       And foo is less than 5
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: lessThan run against a non contradicting not lessThan should be successful (lessThan 5 AND not lessThan 1)
     Given foo is less than 5
       And foo is anything but less than 1
       And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |

Scenario: not lessThan run against a non contradicting lessThan should be successful (not lessThan 2 AND lessThan 5)
     Given foo is anything but less than 2
       And foo is less than 5
       And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |

Scenario: not lessThan run against a non contradicting not lessThan should be successful (not lessThan 5 AND not lessThan 5)
     Given foo is anything but less than 5
       And foo is anything but less than 5
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 5   |
       | 6   |
       | 7   |
       | 8   |
       | 9   |

Scenario: lessThan run against a contradicting not lessThan should only only generate string, temporal and null (lessThan 2 AND not lessThan 2)
     Given foo is less than 2
       And foo is anything but less than 2
     Then the following data should be generated:
       | foo  |
       | null |

#lessThanOrEqual
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting lessThanOrEqualTo should be successful (lessThan 6 AND lessThanOrEqualTo 5)
     Given foo is less than 6
       And foo is less than or equal to 5
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: lessThan run against a non contradicting not lessThanOrEqualTo should be successful (lessThan 10 AND not lessThanOrEqualTo 2)
     Given foo is less than 10
       And foo is anything but less than or equal to 2
       And foo is anything but null
     Then the following data should be generated:
       | foo      |
       | 3        |
       | 4        |
       | 5        |
       | 6        |
       | 7        |
       | 8        |
       | 9        |

Scenario: not lessThan run against a non contradicting lessThanOrEqualTo should be successful (not lessThan 2 AND lessThanOrEqualTo 10)
     Given foo is anything but less than 2
       And foo is less than or equal to 10
       And foo is anything but null
     Then the following data should be generated:
       | foo      |
       | 2        |
       | 3        |
       | 4        |
       | 5        |
       | 6        |
       | 7        |
       | 8        |
       | 9        |
       | 10       |

Scenario: not lessThan run against a non contradicting not lessThanOrEqualTo should be successful (not lessThan 3 AND not lessThanOrEqualTo 4)
     Given foo is anything but less than 3
       And foo is anything but less than or equal to 4
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 5   |
       | 6   |
       | 7   |
       | 8   |
       | 9   |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a contradicting lessThanOrEqualTo should only only generate string, temporal and null (lessThan 1 AND lessThanOrEqualTo 1)
     Given foo is less than 1
       And foo is less than or equal to 1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: not lessThan run against a contradicting not lessThanOrEqualTo should only only generate string, temporal and null (lessThan 2 AND not lessThanOrEqualTo 3)
     Given foo is less than 2
       And foo is anything but less than or equal to 3
     Then the following data should be generated:
       | foo  |
       | null |

#granularTo
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting granularTo should be successful (lessThan 2 AND granularTo 1)
     Given foo is less than 2
       And foo is granular to 1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

@ignore #588: unsure on what the output should be here
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting not granularTo should be successful (lessThan 1 AND not granularTo 1)
     Given foo is less than 1
       And foo is anything but granular to 1
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: not lessThan run against a non contradicting granularTo should be successful (not lessThan 4 AND granularTo 1)
     Given foo is anything but less than 4
       And foo is granular to 1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 4   |
       | 5   |
       | 6   |
       | 7   |
       | 8   |

@ignore #588: unsure on what the output should be here
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: not lessThan run against a non contradicting not granularTo should be successful (not lessThan 5 AND not granularTo 1)
     Given foo is anything but less than 5
       And foo is anything but granular to 1
       And the generator can generate at most 5 rows
     Then some data should be generated

#after
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting after should be successful (lessThan 1 AND after 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is after 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting not after should be successful (lessThan 1 AND not after 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is anything but after 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

#afterOrAt
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting after should be successful (lessThan 1 AND afterOrAt 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is after or at 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting not after should be successful (lessThan 1 AND not afterOrAt 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is anything but after or at 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

#before
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting after should be successful (lessThan 1 AND before 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is before 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting not after should be successful (lessThan 1 AND not before 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is anything but before 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

#beforeOrAt
@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting after should be successful (lessThan 1 AND beforeOrAt 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is before or at 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: lessThan run against a non contradicting not after should be successful (lessThan 1 AND not beforeOrAt 2019-01-01T00:00:00.00)
     Given foo is less than 1
       And foo is anything but before or at 2019-01-01T00:00:00.000
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |
