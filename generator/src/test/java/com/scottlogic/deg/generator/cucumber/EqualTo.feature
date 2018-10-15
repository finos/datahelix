Feature: User can specify that a value is equalTo a required value

  Background:
    Given the generation strategy is full

  Scenario: test1
    Given there is a field foo
    And foo is equal to "String24£"
    And foo is of type "string"
    Then the following data should be generated:
      | foo |
      |"String24£"|



  Scenario: test2
    Given there is a field foo
    When foo is equal to 23
    And foo is of type "numeric"
    Then the following data should be generated:
      | foo |
      | 23  |

  Scenario:
    Given there is a field foo
    And foo is equal to 2018-01-03T00:00:00.000
    And foo is of type "temporal"
    And foo is formatted as "%tF"
    Then the following data should be generated:
      | foo |
      | 2018-01-03 |