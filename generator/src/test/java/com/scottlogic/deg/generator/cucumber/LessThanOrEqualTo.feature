Feature: User can specify that a numeric value is lower than, or equal to, a specified threshold

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "numeric"

@ignore #594
Scenario: Running a 'lessThanOrEqualTo' request that includes positive integer should be successful
     Given foo is less than or equal to 10
       And the generator can generate at most 10 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 2    |
       | 3    |
       | 4    |
       | 5    |
       | 6    |
       | 7    |
       | 8    |
       | 9    |
       | 10   |

@ignore #594
Scenario: Running a 'lessThanOrEqualTo' request that includes positive decimal should be successful
     Given foo is less than or equal to 10.0
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 10.0 |
       | 9.9  |
       | 9.8  |
       | 9.7  |
       | 9.6  |

@ignore #594
Scenario: Running a 'lessThanOrEqualTo' request that includes negative integer should be successful
     Given foo is less than or equal to -10
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | -10  |
       | -9   |
       | -8   |
       | -7   |

@ignore #594
Scenario: Running a 'lessThanOrEqualTo' request that includes 0 should be successful
     Given foo is less than or equal to 0
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 0    |
       | -1   |
       | -2   |
       | -3   |

Scenario: Running a 'lessThanOrEqualTo' request that includes a string should fail
     Given foo is less than or equal to "Zero"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'lessThanOrEqualTo' request that includes an empty string should fail
     Given foo is less than or equal to ""
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'lessThanOrEqualTo' request that specifies null should be unsuccessful
     Given foo is less than or equal to null
     Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
       And no data is created

@ignore #594
Scenario: lessThanOrEqualTo run against a non contradicting lessThanOrEqualToOrEqualTo should be successful
     Given foo is less than or equal to 5
       And foo is less than or equal to 5
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 5    |
       | 4    |
       | 3    |
       | 2    |

Scenario: lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
     Given foo is less than or equal to 5
       And foo is anything but less than or equal to 3
     Then the following data should be generated:
       | foo  |
       | null |
       | 5    |
       | 4    |

@ignore #626
Scenario: not lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
     Given foo is anything but less than or equal to 3
       And foo is anything but less than or equal to 3
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 5    |
       | 6    |
       | 7    |

Scenario: lessThanOrEqualTo run against a contradicting not lessThanOrEqualToOrEqualTo should only only generate null
     Given foo is less than or equal to 3
       And foo is anything but less than or equal to 3
     Then the following data should be generated:
       | foo  |
       | null |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting granularTo should be successful
     Given foo is less than or equal to 3
       And foo is granular to 1
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 3    |
       | 2    |
       | 1    |
       | 0    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting not granularTo should be successful
     Given foo is less than or equal to 3
       And foo is anything but granular to 0.1
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 3    |
       | 2    |
       | 1    |
       | 0    |

@ignore #626
Scenario: not lessThanOrEqualTo run against a non contradicting granularTo should be successful
     Given foo is anything but less than or equal to 3
       And foo is granular to 1
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 5    |
       | 6    |
       | 7    |

@ignore #626
Scenario: not lessThanOrEqualTo run against a non contradicting not granularTo should be successful
     Given foo is anything but less than or equal to 3
       And foo is anything but granular to 0.1
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 5    |
       | 6    |
       | 7    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting after should be successful
     Given foo is less than or equal to 5
       And foo is after 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting not after should be successful
     Given foo is less than or equal to 5
       And foo is anything but after 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting afterOrAt should be successful
     Given foo is less than or equal to 5
       And foo is after or at 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting not afterOrAt should be successful
     Given foo is less than or equal to 5
       And foo is anything but after or at 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting before should be successful
     Given foo is less than or equal to 5
       And foo is before 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting not before should be successful
     Given foo is less than or equal to 5
       And foo is anything but before 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting beforeOrAt should be successful
     Given foo is less than or equal to 5
       And foo is before or at 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |

@ignore #626 #594
Scenario: lessThanOrEqualTo run against a non contradicting not beforeOrAt should be successful
     Given foo is less than or equal to 5
       And foo is anything but before or at 2019-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | 4    |
       | 3    |
       | 2    |
       | 1    |