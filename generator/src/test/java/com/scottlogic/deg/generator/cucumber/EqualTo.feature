Feature: User can specify that a value is equalTo a required value

  Background:
    Given the generation strategy is full

  Scenario: EqualTo operator can be used to generate a string
    Given there is a field foo
    And foo is equal to "String24£"
    Then the following data should be generated:
      | foo |
      |"String24£"|


  Scenario: EqualTo operator can be used to generate a number
    Given there is a field foo
    When foo is equal to 23
    Then the following data should be generated:
      | foo |
      | 23  |

