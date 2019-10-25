Feature: Values can be specified by using any of to set multiple constraints

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo

  Scenario: User requires to create a field with strings that conform to one or many constraints
    Given Any Of the next 3 constraints
    And foo is equal to "Test0"
    And foo is in set:
      | "Test1" |
      | "Test2" |
      | "Test3" |
      | "Test4" |
      | "Test5" |
    And foo is matching regex /[a-b]{4}/
    And foo has type "string"
    Then the following data should be generated:
      | foo     |
      | "Test0" |
      | "Test1" |
      | "Test2" |
      | "Test3" |
      | "Test4" |
      | "Test5" |
      | "aaaa"  |
      | "aaba"  |
      | "aabb"  |
      | "aaab"  |
      | "abaa"  |
      | "abba"  |
      | "abbb"  |
      | "abab"  |
      | "baaa"  |
      | "baba"  |
      | "babb"  |
      | "baab"  |
      | "bbaa"  |
      | "bbba"  |
      | "bbbb"  |
      | "bbab"  |

  Scenario: When user requires creation of a field with strings that contain multiple contradictory sets of one or many constraints no data should be generated
    Given Any Of the next 3 constraints
    And foo is equal to "Test0"
    And foo is in set:
      | "Test1" |
      | "Test2" |
      | "Test3" |
      | "Test4" |
      | "Test5" |
    And foo is matching regex /[a-b]{4}/
    And Any Of the next 2 constraints
    And foo is equal to "Test6"
    And foo is in set:
      | "Test7" |
      | "Test8" |
      | "Test9" |
    And foo has type "string"
    Then no data is created

  Scenario: Running an 'anyOf' request that contains a valid nested anyOf request should be successful
    Given Any Of the next 2 constraints
    And foo is of length 1
    And Any Of the next 2 constraints
    And foo is of length 3
    And foo is of length 5
    And foo is in set:
      | "1"     |
      | "22"    |
      | "333"   |
      | "4444"  |
      | "55555" |
    And foo has type "string"
    Then the following data should be generated:
      | foo     |
      | "1"     |
      | "333"   |
      | "55555" |
    And the following data should not be included in what is generated:
      | "22"   |
      | "4444" |

  Scenario: Running an 'anyOf' request that contains a valid nested allOf request should be successful
    Given Any Of the next 2 constraints
    And foo is of length 1
    And All Of the next 2 constraints
    And foo is longer than 3
    And foo is shorter than 5
    And foo is in set:
      | "1"     |
      | "22"    |
      | "333"   |
      | "4444"  |
      | "55555" |
    And foo has type "string"
    Then the following data should be generated:
      | foo    |
      | "1"    |
      | "4444" |
    And the following data should not be included in what is generated:
      | "22"    |
      | "333"   |
      | "55555" |

  Scenario: Running an 'anyOf' request that contains an invalid nested anyOf request should fail with an error message
    Given Any Of the next 2 constraints
    And foo is of length 1
    And Any Of the next 1 constraints
    And foo is of length -1
    And foo has type "string"
    Then the profile is invalid because "String length must have a value >= 0, currently is -1"
    And no data is created

  Scenario: Running an 'anyOf' request that contains an invalid nested allOf request should fail with an error message
    Given Any Of the next 2 constraints
    And foo is of length 1
    And All Of the next 1 constraints
    And foo is of length -1
    And foo has type "string"
    Then the profile is invalid because "String length must have a value >= 0, currently is -1"
    And no data is created

  Scenario: Running an 'anyOf' request that contains an valid nested allOf request should generate data
    Given Any Of the next 2 constraints
    And foo is of length 1
    And All Of the next 2 constraints
    And foo is longer than 3
    And foo is shorter than 2
    And foo has type "string"
    And foo is in set:
      | "a"  |
      | "aa" |
      | "9"  |
      | "a1" |
      | "B"  |
    Then the following data should be generated:
      | foo |
      | "a" |
      | "9" |
      | "B" |
