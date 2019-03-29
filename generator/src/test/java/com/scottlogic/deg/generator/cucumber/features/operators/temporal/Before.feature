Feature: User can specify that a datetime date is lower than, but not equal to, a specified threshold

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "datetime"

#Alone
# Defect 594 "Reverse order of value generation when only upper-bound operators are provided" related to this scenario
@ignore
Scenario: Running a 'before' request over a year threshold should be successful
  Given foo is before 2019-01-01T00:00:00.000Z
  And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                      |
    | 2018-12-31T23:59:59.999Z |
    | 2018-12-31T23:59:59.998Z |
    | 2018-12-31T23:59:59.997Z |
    | 2018-12-31T23:59:59.996Z |
    | 2018-12-31T23:59:59.995Z |

# Defect 594 "Reverse order of value generation when only upper-bound operators are provided" related to this scenario
@ignore
Scenario: Running a 'before' request over a leap year date should be successful
  Given foo is before 2016-03-01T00:00:00.000Z
  And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                      |
    | null                     |
    | 2016-02-29T23:59:59.999Z |
    | 2016-02-29T23:59:59.998Z |
    | 2016-02-29T23:59:59.997Z |
    | 2016-02-29T23:59:59.996Z |

# Defect 594 "Reverse order of value generation when only upper-bound operators are provided" related to this scenario
@ignore
Scenario: Running a 'before' request that specifies the maximum valid system date should be successful
     Given foo is before 9999-12-31T23:59:59.999Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 9999-12-31T23:59:59.998Z |
       | 9999-12-31T23:59:59.997Z |
       | 9999-12-31T23:59:59.996Z |
       | 9999-12-31T23:59:59.995Z |

Scenario: Running a 'before' request that specifies the lowest valid system date should only generate null data
     Given foo is before 0001-01-01T00:00:00.000Z
     Then the following data should be generated:
       | foo                     |
       | null                    |

Scenario: Running a 'before' request that specifies an invalid date should be unsuccessful
     Given foo is before 2019-30-30T00:00:00.000Z
     Then the profile is invalid
       And no data is created

Scenario: Running a 'before' request that specifies an invalid time should be unsuccessful
     Given foo is before 2019-01-01T24:00:00.000Z
       Then the profile is invalid
     And no data is created

Scenario: Running a 'before' request that specifies an invalid leap year should be unsuccessful
     Given foo is before 2019-02-29T00:00:00.000Z
       Then the profile is invalid
     And no data is created

#before
@ignore #594 generation should be descending when upper-bound only is provided
Scenario: 'before' run against a non contradicting 'before' should be successful
     Given foo is before 2019-01-01T00:00:00.000Z
       And foo is before 2018-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | 2017-12-31T23:59:59.999Z |
       | 2017-12-31T23:59:59.998Z |
       | 2017-12-31T23:59:59.997Z |
       | 2017-12-31T23:59:59.996Z |
       | 2017-12-31T23:59:59.995Z |

# Defect 594 "Reverse order of value generation when only upper-bound operators are provided" related to this scenario
# Defect 141 "Implement granularity for datetimes" related to this issue
@ignore
Scenario: 'before' run against a non contradicting not 'before' should be successful
     Given foo is before 2019-01-02T00:00:00.000Z
       And foo is anything but before 2019-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 2019-01-01T23:59:59.999Z |
       | 2019-01-01T23:59:59.998Z |
       | 2019-01-01T23:59:59.997Z |
       | 2019-01-01T23:59:59.996Z |

Scenario: not 'before' run against a non contradicting not 'before' should be successful
     Given foo is anything but before 2019-01-01T00:00:00.000Z
       And foo is anything but before 2018-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | 2019-01-01T00:00:00.000Z |
       | 2019-01-01T00:00:00.001Z |
       | 2019-01-01T00:00:00.002Z |
       | 2019-01-01T00:00:00.003Z |
       | 2019-01-01T00:00:00.004Z |

Scenario: 'before' run against a contradicting not 'before' should only only generate string, numeric and null
     Given foo is before 2019-01-01T00:00:00.000Z
       And foo is anything but before 2019-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |

#beforeOrAt
@ignore #594 generation should be descending when upper-bound only is provided
Scenario: 'before' run against a non contradicting 'beforeOrAt' should be successful
     Given foo is before 2019-01-01T00:00:00.000Z
       And foo is before or at 2018-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | 2018-01-01T00:00:00.000Z |
       | 2017-12-31T23:59:59.999Z |
       | 2017-12-31T23:59:59.998Z |
       | 2017-12-31T23:59:59.997Z |
       | 2017-12-31T23:59:59.996Z |

Scenario: 'before' run against a non contradicting not 'beforeOrAt' should be successful
     Given foo is before 2019-01-01T00:00:00.000Z
       And foo is anything but before or at 2018-12-31T23:59:59.996Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 2018-12-31T23:59:59.999Z |
       | 2018-12-31T23:59:59.998Z |
       | 2018-12-31T23:59:59.997Z |

Scenario: not 'before' run against a non contradicting 'beforeOrAt' should be successful
     Given foo is anything but before 2018-12-31T23:59:59.997Z
       And foo is before or at 2019-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 2019-01-01T00:00:00.000Z |
       | 2018-12-31T23:59:59.999Z |
       | 2018-12-31T23:59:59.998Z |
       | 2018-12-31T23:59:59.997Z |

Scenario: not 'before' run against a non contradicting not 'beforeOrAt' should be successful
     Given foo is anything but before 2019-01-01T00:00:00.000Z
       And foo is anything but before or at 2018-01-01T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                      |
       | 2019-01-01T00:00:00.000Z |
       | 2019-01-01T00:00:00.001Z |
       | 2019-01-01T00:00:00.002Z |
       | 2019-01-01T00:00:00.003Z |
       | 2019-01-01T00:00:00.004Z |

Scenario: 'before' run against a contradicting not 'beforeOrAt' should only only generate string, numeric and null
     Given foo is before 2019-01-01T00:00:00.000Z
       And foo is anything but before or at 2019-01-02T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |

Scenario: Running a 'before' request that specifies null should be unsuccessful
    Given foo is before null
    Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
      And no data is created

Scenario: Running a 'before' request that specifies the highest valid system date should be unsuccessful
    Given foo is before 0000-01-01T00:00:00.000Z
    Then the profile is invalid because "Date string '0000-01-01T00:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
