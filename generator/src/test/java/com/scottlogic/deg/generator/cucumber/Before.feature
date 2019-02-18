Feature: User can specify that a temporal date is lower than, but not equal to, a specified threshold

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "temporal"

#Alone
# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request that specifies a valid date should be successful
     Given foo is before 2019-02-10T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-02-09T23:59:59.999 |
       | 2019-02-09T23:59:59.998 |
       | 2019-02-09T23:59:59.997 |
       | 2019-02-09T23:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request that specifies a valid time should be successful
     Given foo is before 2019-01-01T12:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-01-01T11:59:59.999 |
       | 2019-01-01T11:59:59.998 |
       | 2019-01-01T11:59:59.997 |
       | 2019-01-01T11:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a year threshold should be successful
     Given foo is before 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2018-12-31T23:59:59.999 |
       | 2018-12-31T23:59:59.998 |
       | 2018-12-31T23:59:59.997 |
       | 2018-12-31T23:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a month threshold should be successful
     Given foo is before 2019-02-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-01-31T23:59:59.999 |
       | 2019-01-31T23:59:59.998 |
       | 2019-01-31T23:59:59.997 |
       | 2019-01-31T23:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a day threshold should be successful
     Given foo is before 2019-02-02T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-02-01T23:59:59.999 |
       | 2019-02-01T23:59:59.998 |
       | 2019-02-01T23:59:59.997 |
       | 2019-02-01T23:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over an hour threshold should be successful
     Given foo is before 2019-02-02T13:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-02-02T12:59:59.999 |
       | 2019-02-02T12:59:59.998 |
       | 2019-02-02T12:59:59.997 |
       | 2019-02-02T12:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a minute threshold should be successful
     Given foo is before 2019-02-02T13:20:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-02-02T13:19:59.999 |
       | 2019-02-02T13:19:59.998 |
       | 2019-02-02T13:19:59.997 |
       | 2019-02-02T13:19:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a second threshold should be successful
     Given foo is before 2019-02-02T13:20:19.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-02-02T13:20:18.999 |
       | 2019-02-02T13:20:18.998 |
       | 2019-02-02T13:20:18.997 |
       | 2019-02-02T13:20:18.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a leap year date should be successful
     Given foo is before 2016-03-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2016-02-29T23:59:59.999 |
       | 2016-02-29T23:59:59.998 |
       | 2016-02-29T23:59:59.997 |
       | 2016-02-29T23:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request over a non-leap year date should be successful
     Given foo is before 2017-03-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2017-02-28T23:59:59.999 |
       | 2017-02-28T23:59:59.998 |
       | 2017-02-28T23:59:59.997 |
       | 2017-02-28T23:59:59.996 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: Running a 'before' request that specifies the maximum valid system date should be successful
     Given foo is before 9999-12-31T23:59:59.999
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 9999-12-31T23:59:59.998 |
       | 9999-12-31T23:59:59.997 |
       | 9999-12-31T23:59:59.996 |
       | 9999-12-31T23:59:59.995 |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
# Defect 607 "Setting Before 0001-01-01T00:00:00.000 loops round to december of that year" related to this scenario
# Defect 608 "Setting a date of 0000-01-01T00:00:00.000 results in an error" related to this scenario
@ignore
Scenario: Running a 'before' request that specifies the lowest valid system date should only generate null data
     Given foo is before 0000-01-01T00:00:00.000
     Then the following data should be generated:
       | foo                     |
       | null                    |

Scenario: Running a 'before' request that specifies an invalid date should be unsuccessful
     Given foo is before 2019-30-30T00:00:00.000
     Then the profile is invalid
       And no data is created

Scenario: Running a 'before' request that specifies an invalid time should be unsuccessful
     Given foo is before 2019-01-01T24:00:00.000
       Then the profile is invalid
     And no data is created

# Defect 610 "Setting before using an invalid leap year date does not fail as expected" related to this scenario
@ignore
Scenario: Running a 'before' request that specifies an invalid leap year should be unsuccessful
     Given foo is before 2019-02-29T00:00:00.000
       Then the profile is invalid
     And no data is created

#before
# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'before' run against a non contradicting 'before' should be successful
     Given foo is before 2019-01-01T00:00:00.000
       And foo is before 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2017-12-31T23:59:59.999 |
       | 2017-12-31T23:59:59.998 |
       | 2017-12-31T23:59:59.997 |
       | 2017-12-31T23:59:59.996 |

# Defect 594 "Reverse order of value generation when only upper-bound operators are provided" related to this scenario
# Defect 141 "Implement granularity for temporal values" related to this issue
@ignore
Scenario: 'before' run against a non contradicting not 'before' should be successful
     Given foo is before 2019-01-02T00:00:00.000
       And foo is anything but before 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-01-01T23:59:59.999 |
       | 2019-01-01T23:59:59.998 |
       | 2019-01-01T23:59:59.997 |
       | 2019-01-01T23:59:59.996 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: not 'before' run against a non contradicting not 'before' should be successful
     Given foo is anything but before 2019-01-01T00:00:00.000
       And foo is anything but before 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-01-01T00:00:00.001 |
       | 2019-01-01T00:00:00.002 |
       | 2019-01-01T00:00:00.003 |
       | 2019-01-01T00:00:00.004 |
# Defect 613 "Request for simultaneously Before and not Before a date brings back that date" related to this scenario
@ignore
Scenario: 'before' run against a contradicting not 'before' should only only generate string, numeric and null
     Given foo is before 2019-01-01T00:00:00.000
       And foo is anything but before 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |

#beforeOrAt
# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'before' run against a non contradicting 'beforeOrAt' should be successful
     Given foo is before 2019-01-01T00:00:00.000
       And foo is before or at 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2018-01-01T00:00:00.000 |
       | 2017-12-31T23:59:59.999 |
       | 2017-12-31T23:59:59.998 |
       | 2017-12-31T23:59:59.997 |

# Defect 614 "Before and not BeforeOrAt run in full generation mode doesn't create all expected values" related to this scenario
@ignore
Scenario: 'before' run against a non contradicting not 'beforeOrAt' should be successful
     Given foo is before 2019-01-01T00:00:00.000
       And foo is anything but before or at 2018-12-31T23:59:59.996
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2018-12-31T23:59:59.999 |
       | 2018-12-31T23:59:59.998 |
       | 2018-12-31T23:59:59.997 |

# Defect 614 "Before and not BeforeOrAt run in full generation mode doesn't create all expected values" related to this scenario
@ignore
Scenario: not 'before' run against a non contradicting 'beforeOrAt' should be successful
     Given foo is anything but before 2018-12-31T23:59:59.997
       And foo is before or at 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2019-01-01T00:00:00.000 |
       | 2018-12-31T23:59:59.999 |
       | 2018-12-31T23:59:59.998 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: not 'before' run against a non contradicting not 'beforeOrAt' should be successful
     Given foo is anything but before 2019-01-01T00:00:00.000
       And foo is anything but before or at 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2018-01-01T00:00:00.000 |
       | 2017-12-31T23:59:59.999 |
       | 2017-12-31T23:59:59.998 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'before' run against a contradicting 'beforeOrAt' should only only generate string, numeric and null
     Given foo is before 2019-01-01T00:00:00.000
       And foo is before or at 2019-01-02T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |

# Defect 600 "Running a before constraint in full generation mode generates interesting data" related to this scenario
@ignore
Scenario: 'before' run against a contradicting not 'beforeOrAt' should only only generate string, numeric and null
     Given foo is before 2019-01-01T00:00:00.000
       And foo is anything but before or at 2019-01-02T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | null                    |