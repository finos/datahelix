Feature: User can specify that a string length is lower than, a specified number of characters

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "string"

Scenario: Running a 'shorterThan' request using a number to specify a the length of a generated string should be successful
     Given foo is shorter than 5
       And foo is matching regex /[x]{1,5}/
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo      |
       | null     |
       | "x"      |
       | "xx"     |
       | "xxx"    |
       | "xxxx"   |

Scenario: Running a 'shorterThan' request using a number (zero) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than 0
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using a number (negative number) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than -1
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using a number (decimal number) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than 1.1
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using a string (number) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than "5"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using an empty string "" to specify a the length of a generated string field should fail with an error message
     Given foo is shorter than ""
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using null to specify a the length of a generated string field should fail with an error message
     Given foo is shorter than null
     Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
       And no data is created

Scenario: shorterThan run against a non contradicting shorterThan should be successful
     Given foo is shorter than 4
       And foo is shorter than 3
       And foo is matching regex /[2]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "2"  |
       | "22" |

Scenario: shorterThan run against a non contradicting not shorterThan should be successful
     Given foo is shorter than 5
       And foo is anything but shorter than 3
       And foo is matching regex /[a]{1,5}/
     Then the following data should be generated:
       | foo    |
       | null   |
       | "aaa"  |
       | "aaaa" |

Scenario: not shorterThan run against a non contradicting not shorterThan should be successful
     Given foo is anything but shorter than 2
       And foo is anything but shorter than 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo     |
       | null    |
       | "xxx"   |
       | "xxxx"  |
       | "xxxxx" |

Scenario: shorterThan run against a contradicting not shorterThan should only generate null
     Given foo is shorter than 2
       And foo is anything but shorter than 2
     Then the following data should be generated:
       | foo     |
       | null    |

Scenario: shorterThan run against a non contradicting greaterThan should be successful
     Given foo is shorter than 2
       And foo is greater than 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not greaterThan should be successful
     Given foo is shorter than 2
       And foo is anything but greater than 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting greaterThanOrEqualTo should be successful
     Given foo is shorter than 2
       And foo is greater than or equal to 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not greaterThanOrEqualTo should be successful
     Given foo is shorter than 2
       And foo is anything but greater than or equal to 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting lessThan should be successful
     Given foo is shorter than 2
       And foo is less than 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not lessThan should be successful
     Given foo is shorter than 2
       And foo is anything but less than 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting lessThanOrEqualTo should be successful
     Given foo is shorter than 2
       And foo is less than or equal to 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not lessThanOrEqualTo should be successful
     Given foo is shorter than 2
       And foo is anything but less than or equal to 3
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting granularTo should be successful
     Given foo is shorter than 2
       And foo is granular to 1
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not granularTo should be successful
     Given foo is shorter than 2
       And foo is anything but granular to 1
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting after should be successful
     Given foo is shorter than 2
       And foo is after 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not after should be successful
     Given foo is shorter than 2
       And foo is anything but after 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting afterOrAt should be successful
     Given foo is shorter than 2
       And foo is after or at 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not afterOrAt should be successful
     Given foo is shorter than 2
       And foo is anything but after or at 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting before should be successful
     Given foo is shorter than 2
       And foo is before 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not before should be successful
     Given foo is shorter than 2
       And foo is anything but before 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting beforeOrAt should be successful
     Given foo is shorter than 2
       And foo is before or at 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |

Scenario: shorterThan run against a non contradicting not beforeOrAt should be successful
     Given foo is shorter than 2
       And foo is anything but before or at 2019-01-01T00:00:00.000Z
       And foo is matching regex /[x]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "x"  |