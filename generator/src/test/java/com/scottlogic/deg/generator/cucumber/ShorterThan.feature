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

Scenario: Running a 'shorterThan' request using a string (a-z character) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than "five"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using a string (A-Z character) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than "FIVE"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using a string (A-Z character) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than "FIVE"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using a string (special character) to specify a the length of a generated string should fail with an error message
     Given foo is shorter than "!"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using an empty string "" to specify a the length of a generated string field should fail with an error message
     Given foo is shorter than ""
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request using null to specify a the length of a generated string field should fail with an error message
     Given foo is shorter than null
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'shorterThan' request alongside a non-contradicting inSet constraint should be successful
     Given foo is shorter than 5
       And foo is in set:
       | "1234" |
       | "123"  |
     Then the following data should be generated:
       | foo    |
       | null   |
       | "1234" |
       | "123"  |

Scenario: Running a 'shorterThan' request alongside a non-contradicting inSet constraint should produce null
     Given foo is shorter than 5
       And foo is in set:
       | "12345"  |
       | "123456" |
     Then the following data should be generated:
       | foo    |
       | null   |

Scenario: Running a 'shorterThan' request alongside a null constraint should generate null
     Given foo is shorter than 5
       And foo is null
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'shorterThan' request alongside a non-contradicting matchingRegex constraint should be successful
     Given foo is shorter than 3
       And foo is matching regex /[☠]{2}/
     Then the following data should be generated:
       | foo    |
       | null   |
       | "☠☠" |

Scenario: Running a 'shorterThan' request alongside a contradicting matchingRegex constraint should generate null
     Given foo is shorter than 2
       And foo is matching regex /[💾]{2}/
     Then the following data should be generated:
       | foo    |
       | null   |

Scenario: Running a 'shorterThan' request alongside a non-contradicting containingRegex constraint should be successful
     Given foo is shorter than 3
       And foo is containing regex /[Æ]{2}/
       And the generator can generate at most 2 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "ÆÆ" |

Scenario: Running a 'shorterThan' request alongside a contradicting containingRegex constraint should generate null
     Given foo is shorter than 1
       And foo is containing regex /[Ū]{2}/
      Then the following data should be generated:
        | foo    |
        | null   |

Scenario: Running a 'shorterThan' request alongside a non-contradicting ofLength constraint should be successful
     Given foo is shorter than 3
       And foo is of length 2
       And foo is matching regex /[♀]{0,2}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "♀♀" |

Scenario: Running a 'shorterThan' request alongside a contradicting ofLength (too short) constraint should produce null
     Given foo is shorter than 3
       And foo is of length 10
     Then the following data should be generated:
      | foo  |
      | null |

Scenario: Running a 'shorterThan' request alongside a non-contradicting longerThan constraint should be successful
     Given foo is shorter than 3
       And foo is longer than 1
       And foo is matching regex /[♀]{0,3}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "♀♀" |

Scenario: Running a 'shorterThan' request alongside a contradicting longerThan (too long) constraint should produce null
     Given foo is shorter than 3
       And foo is longer than 10
     Then the following data should be generated:
      | foo  |
      | null |

Scenario: Running a 'shorterThan' request alongside a non-contradicting shorterThan constraint should be successful
     Given foo is shorter than 4
       And foo is shorter than 3
       And foo is matching regex /[♀]{1,5}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "♀"  |
       | "♀♀" |

@ignore
Scenario: Running a 'shorterThan' request alongside a greaterThan constraint should be successful
     Given foo is shorter than 10
       And foo is greater than 8
       And foo is matching regex /[a]{1,10}/
     Then the following data should be generated:
       | foo         |
       | "aaaaaaaaa" |
       | null        |

@ignore
Scenario: Running a 'shorterThan' request alongside a greaterThanOrEqualTo constraint should be successful
     Given foo is shorter than 3
       And foo is greater than or equal to 2
       And foo is matching regex /[a]{1,10}/
     Then the following data should be generated:
       | foo    |
       | "aa"   |
       | null   |

Scenario: Running a 'shorterThan' request alongside a lessThan constraint should be successful
     Given foo is shorter than 2
       And foo is less than 15
       And foo is matching regex /[a]{1,10}/
     Then the following data should be generated:
       | foo    |
       | "a"    |
       | null   |

Scenario: Running a 'shorterThan' request alongside a lessThanOrEqualTo constraint should be successful
     Given foo is shorter than 2
       And foo is less than or equal to 19
       And foo is matching regex /[a]{1,10}/
     Then the following data should be generated:
       | foo    |
       | "a"    |
       | null   |

@ignore
Scenario: Running a 'shorterThan' request as part of a non-contradicting anyOf constraint should be successful
     Given there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "shorterThan", "value": 2 },
         { "field": "foo", "is": "shorterThan", "value": 3 }
       ]}
       """
       And foo is matching regex /[%]{1,10}/
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "%"  |
       | "%%" |

Scenario: Running a 'shorterThan' request as part of a non-contradicting allOf constraint should be successful
     Given there is a constraint:
       """
       { "allOf": [
         { "field": "foo", "is": "shorterThan", "value": 3 },
         { "field": "foo", "is": "shorterThan", "value": 2 }
       ]}
       """
       And foo is matching regex /[%]{1,10}/
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "%"  |

Scenario: Running a 'shorterThan' request using a number round (decimal number) to specify a the length of a generated string should be successful
    Given foo is shorter than 2.0
    And foo is in set:
      | "xxx" |
      | "xx"  |
      | "x"   |
    Then the following data should be generated:
      | foo  |
      | null |
      | "x"  |