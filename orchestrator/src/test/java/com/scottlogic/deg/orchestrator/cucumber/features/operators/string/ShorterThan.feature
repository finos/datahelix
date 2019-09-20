Feature: User can specify that a string length is lower than, a specified number of characters

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo has type "string"

  Scenario: Running a 'shorterThan' request using a number to specify a the length of a generated string should be successful
    Given foo is shorter than 5
    And foo is matching regex /[x]{1,5}/
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo    |
      | null   |
      | "x"    |
      | "xx"   |
      | "xxx"  |
      | "xxxx" |

  Scenario: Running a 'shorterThan' request using a number (zero) to specify a the length of a generated string should fail with an error message
    Given foo is shorter than -1
    Then the profile is invalid because "Field \[foo\]: shorterThan constraint must have an operand/value >= 0, currently is -1"
    And no data is created

  Scenario: Running a 'shorterThan' request using a number (negative number) to specify a the length of a generated string should fail with an error message
    Given foo is shorter than -1
    Then the profile is invalid because "Field \[foo\]: shorterThan constraint must have an operand/value >= 0, currently is -1"
    And no data is created

  Scenario: Running a 'shorterThan' request using a number (decimal number) to specify a the length of a generated string should fail with an error message
    Given foo is shorter than 1.1
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be an integer but was a decimal with value `1.1`"
    And no data is created

  Scenario: Running a 'shorterThan' request using a string (number) to specify a the length of a generated string should fail with an error message
    Given foo is shorter than "5"
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be an Integer but was a String with value `5`"
    And no data is created

  Scenario: Running a 'shorterThan' request using an empty string "" to specify a the length of a generated string field should fail with an error message
    Given foo is shorter than ""
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be an Integer but was a String with value ``"
    And no data is created

  Scenario: Running a 'shorterThan' request using null to specify a the length of a generated string field should fail with an error message
    Given foo is shorter than null
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"
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
      | foo  |
      | null |

  Scenario: shorterThan with maximum permitted value should be successful
    Given foo is shorter than 1001
    And the generation strategy is random
    And the generator can generate at most 1 rows
    And foo is anything but null
    Then foo contains strings of length between 0 and 1000 inclusively

  Scenario: shorterThan with value larger than maximum permitted should fail with an error message
    Given foo is shorter than 1002
    Then the profile is invalid because "Field \[foo\]: shorterThan constraint must have an operand/value <= 1000, currently is 1002"

  Scenario: Running a 'shorterThan' request with a value less than implicit max (255) should generate data of length between 0 and value
    Given foo has type "string"
    And foo is shorter than 254
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 0 and 253 inclusively

  Scenario: Running a 'shorterThan' request with a value at the implicit max (255) should generate data of length between 0 and value
    Given foo has type "string"
    And foo is shorter than 256
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 0 and 255 inclusively

  Scenario: Running a 'shorterThan' request with a value greater than implicit max (255) should generate data of length between 0 and value
    Given foo is shorter than 257
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 0 and 256 inclusively

  Scenario: Running a 'shorterThan' request with a value less than implicit max (255) should generate data of length between 0 and value
    Given foo has type "string"
    And foo is shorter than 254
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 0 and 253 inclusively

  Scenario: Running a 'shorterThan' request with a value greater than implicit max (255) should generate data of length between 0 and value
    Given foo has type "string"
    And foo is shorter than 256
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 0 and 255 inclusively
