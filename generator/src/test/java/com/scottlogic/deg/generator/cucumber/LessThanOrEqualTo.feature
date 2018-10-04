Feature: User can specify that a numeric value is lower than, or equal to, a specified threshold
    
Scenario: User requires to create a numeric field with data values that are less than or the same as ten
     Given there is a field foo
       And foo is lessThanOrEqualTo 0
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |
       | 8   |
       | 9   |
       | 10  |

Scenario: User requires to create a numeric field with data values that are less than or the same as one but constrained to not be greater than zero
     Given there is a field foo
       And foo is lessThanOrEqualTo 1
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |

Scenario: User requires to create a field with decimal values that are less than or the same as one, specified as an interger
     Given that there is a field foo
       And foo is granularTo 0.1
       And foo is lessThanOrEqualTo 1
       And foo is greaterThan 0
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 0.1 |
       | 0.2 |
       | 0.3 |
       | 0.4 |
       | 0.5 |
       | 0.6 |
       | 0.7 |
       | 0.8 |
       | 0.9 |
       | 1   |

Scenario: User requires to create a field with decimal values that are less than or the same as one, specifed as a decimal
     Given that there is a field foo
       And foo is granularTo 0.1
       And foo is lessThanOrEqualTo 1.0
       And foo is greaterThan 0.0
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 0.1 |
       | 0.2 |
       | 0.3 |
       | 0.4 |
       | 0.5 |
       | 0.6 |
       | 0.7 |
       | 0.8 |
       | 0.9 |
       | 1   |

Scenario: User requires to create a numeric field with data values that are less than or the same as a negative number
     Given there is a field foo
       And foo is lessThanOrEqualTo -10
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | -10 |
       | -9  |
       | -8  |
       | -7  |
       | -6  |
       | -5  |
       | -4  |
       | -3  |
       | -2  |
       | -1  |

Scenario: User requires to create a numeric field with data values that are less than ten and less than or the same as nine
     Given there is a field foo
       And foo is lessThan 10
       And foo is lessThanOrEqualTo 9
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |
       | 8   |
       | 9   |

Scenario: User requires to create a numeric field with data values that are less than or the same as ten and less than nine
     Given there is a field foo
       And foo is lessThanOrEqualTo 10
       And foo is lessThan 9
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |
       | 8   |

Scenario: User requires to create a numeric field with data values that are less than or the same as five and less than or the same as four
     Given there is a field foo
       And foo is lessThanOrEqualTo 5
       And foo is lessThanOrEqualTo 4
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |

Scenario: User attempts to create a numeric field with data value that are less than or the same as zero using an incorrect field value type of string
     Given there is a field foo
       And foo is lessThanOrEqualTo "Zero"
       And foo is not null
     When I ask to generate all values for field foo
     Then I am presented with an error message
        And no data is created