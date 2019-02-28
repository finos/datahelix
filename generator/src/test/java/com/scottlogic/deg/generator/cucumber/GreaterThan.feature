Feature: User can specify that a numeric value is higher than, but not equal to, a specified threshold

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "numeric"

#alone
Scenario: Running a 'greaterThan' request that specifies an integer should be successful
     Given foo is greater than 1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

Scenario: Running a 'greaterThan' request that specifies an integer with trailing zeros should be successful
     Given foo is greater than 100
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 101 |
       | 102 |
       | 103 |
       | 104 |
       | 105 |

Scenario: Running a 'greaterThan' request that specifies a decimal should be successful
     Given foo is greater than 100.1
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo   |
       | 100.2 |
       | 100.3 |
       | 100.4 |
       | 100.5 |
       | 100.6 |

Scenario: Running a 'greaterThan' request that specifies a decimal with trailing zeros should be successful
    Given foo is greater than 100.0
	  And foo is anything but null
      And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo   |
      | 100.1 |
      | 100.2 |
      | 100.3 |
      | 100.4 |
      | 100.5 |

Scenario: Running a 'greaterThan' request that specifies a string should be unsuccessful
    Given foo is greater than "bar"
    Then the profile is invalid
      And no data is created

Scenario: Running a 'greaterThan' request that specifies an empty string should be unsuccessful
    Given foo is greater than ""
    Then the profile is invalid
      And no data is created

Scenario: Running a 'greaterThan' request that specifies null should be unsuccessful
    Given foo is greater than null
    Then the profile is invalid
      And no data is created

  Scenario: User requires negative numbers data
     Given foo is greater than -100
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | -99  |
       | -98  |
       | -97  |
       | -96  |
       | -95  |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: Running a 'greaterThan' request that includes the value zero should be successful
     Given foo is greater than -1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 0   |
       | 1   |
       | 2   |
       | 3   |
       | 4   |

#greaterThan
Scenario: greaterThan run against a non contradicting greaterThan should be successful (greaterThan 2 AND greaterThan 1)
     Given foo is greater than 2
       And foo is greater than 1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |

Scenario: greaterThan run against a non contradicting not greaterThan should be successful (greaterThan 1 AND not greaterThan 5)
     Given foo is greater than 1
       And foo is anything but greater than 5
       And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: not greaterThan run against a non contradicting not greaterThan should be successful (not greaterThan 5 AND not greaterThan 4)
     Given foo is anything but greater than 5
       And foo is anything but greater than 4
       And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: greaterThan run against a contradicting not greaterThan should only only generate string, temporal and null (greaterThan 2 AND not greaterThan 2)
     Given foo is greater than 2
       And foo is anything but greater than 2
     Then the following data should be generated:
       | foo  |
       | null |

#greaterThanOrEqualTo
Scenario: greaterThan run against a non contradicting greaterThanOrEqualTo should be successful (greaterThan 2 AND greaterThanOrEqualTo 1)
     Given foo is greater than 2
       And foo is greater than or equal to 1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |

Scenario: greaterThan run against a non contradicting not greaterThanOrEqualTo should be successful (greaterThan 1 AND not greaterThanOrEqualTo 5)
     Given foo is greater than 1
       And foo is anything but greater than or equal to 5
	   And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: not greaterThan run against a non contradicting not greaterThanOrEqualTo should be successful (not greaterThan 5 AND not greaterThanOrEqualTo 4)
     Given foo is anything but greater than 5
       And foo is anything but greater than or equal to 4
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: greaterThan run against a contradicting not greaterThan should only only generate string, temporal and null (greaterThan 2 AND not greaterThanOrEqualTo 3)
     Given foo is greater than 2
       And foo is anything but greater than or equal to 3
     Then the following data should be generated:
       | foo  |
       | null |

#lessThan
Scenario: greaterThan run against a non contradicting lessThan should be successful (greaterThan 2 AND lessThan 5)
     Given foo is greater than 2
       And foo is less than 5
	   And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 3   |
       | 4   |

Scenario: greaterThan run against a non contradicting not lessThan should be successful (greaterThan 1 AND not lessThan 2)
     Given foo is greater than 1
       And foo is anything but less than 2
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: not greaterThan run against a non contradicting lessThan should be successful (not greaterThan 4 AND lessThan 2)
     Given foo is anything but greater than 4
       And foo is less than 2
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: not greaterThan run against a non contradicting not lessThan should be successful (not greaterThan 5 AND not lessThan 4)
     Given foo is anything but greater than 5
       And foo is anything but less than 4
	   And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 4   |
       | 5   |

Scenario: greaterThan run against a contradicting lessThan should only only generate string, temporal and null (greaterThan 2 AND lessThan 2)
     Given foo is greater than 2
       And foo is less than 2
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: not greaterThan run against a contradicting not lessThan should only only generate string, temporal and null (not greaterThan 2 AND not lessThan 2)
     Given foo is anything but greater than 2
       And foo is anything but less than 2
	   And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 2   |

#lessThanOrEqualTo
Scenario: greaterThan run against a non contradicting lessThanOrEqualTo should be successful (greaterThan 2 AND lessThanOrEqualTo 5)
     Given foo is greater than 2
       And foo is less than or equal to 5
	   And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 3   |
       | 4   |
       | 5   |

Scenario: greaterThan run against a non contradicting not lessThanOrEqualTo should be successful (greaterThan 1 AND not lessThanOrEqualTo 2)
     Given foo is greater than 1
       And foo is anything but less than or equal to 2
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: not greaterThan run against a non contradicting lessThanOrEqualTo should be successful (not greaterThan 4 AND lessThanOrEqualTo 2)
     Given foo is anything but greater than 4
       And foo is less than or equal to 2
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

Scenario: not greaterThan run against a non contradicting not lessThanOrEqualTo should be successful (not greaterThan 5 AND not lessThanOrEqualTo 4)
     Given foo is anything but greater than 5
       And foo is anything but less than or equal to 4
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 5   |

Scenario: greaterThan run against a contradicting lessThanOrEqualTo should only only generate string, temporal and null (greaterThan 2 AND lessThanOrEqualTo 1)
     Given foo is greater than 2
       And foo is less than or equal to 1
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: not greaterThan run against a contradicting not lessThanOrEqualTo should only only generate string, temporal and null (not greaterThan 2 AND not lessThanOrEqualTo 1)
     Given foo is anything but greater than 2
       And foo is anything but less than or equal to 1
	   And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 2   |

#granularTo
Scenario: greaterThan run against a non contradicting granularTo should be successful (greaterThan 2 AND granularTo 1)
     Given foo is greater than 2
       And foo is granular to 1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |

@ignore #588: unsure on what the output should be here
Scenario: greaterThan run against a non contradicting not granularTo should be successful (greaterThan 1 AND not granularTo 1)
     Given foo is greater than 1
       And foo is anything but granular to 1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
Scenario: not greaterThan run against a non contradicting granularTo should be successful (not greaterThan 4 AND granularTo 1)
     Given foo is anything but greater than 4
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

@ignore #589, #594: produces big-integer numbers rather than integer values, not sure why
#588: unsure on what the output should be here
Scenario: not greaterThan run against a non contradicting not granularTo should be successful (not greaterThan 5 AND not granularTo 1)
     Given foo is anything but greater than 5
       And foo is anything but granular to 1
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo         |
       | -2147482624 |
       | -2147482623 |
       | -2147482622 |
       | -2147482621 |
       | -2147482620 |

#after
Scenario: greaterThan run against a non contradicting after should be successful (greaterThan 1 AND after 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is after 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

Scenario: greaterThan run against a non contradicting not after should be successful (greaterThan 1 AND not after 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is anything but after 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

#afterOrAt
Scenario: greaterThan run against a non contradicting afterOrAt should be successful (greaterThan 1 AND afterOrAt 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is after or at 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

Scenario: greaterThan run against a non contradicting not afterOrAt should be successful (greaterThan 1 AND not afterOrAt 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is anything but after or at 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

#before
Scenario: greaterThan run against a non contradicting before should be successful (greaterThan 1 AND before 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is before 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

Scenario: greaterThan run against a non contradicting not before should be successful (greaterThan 1 AND not before 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is anything but before 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

#beforeOrAt
Scenario: greaterThan run against a non contradicting beforeOrAt should be successful (greaterThan 1 AND beforeOrAt 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is before or at 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |

Scenario: greaterThan run against a non contradicting not beforeOrAt should be successful (greaterThan 1 AND not beforeOrAt 2019-01-01T00:00:00.00)
     Given foo is greater than 1
       And foo is anything but before or at 2019-01-01T00:00:00.000
	   And foo is anything but null
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
