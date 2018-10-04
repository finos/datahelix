Feature: User can specify that a value either matches or contains a specified regex

  Scenario:
    Given there is a field foo
    And foo is matching regex /a{1,3}/
    And foo is not null
    Then the following data should be generated:
      | foo |
      | a   |
      | aa  |
      | aaa |


