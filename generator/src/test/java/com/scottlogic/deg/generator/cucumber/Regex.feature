Feature: User can specify that a value either matches or contains a specified regex

  Scenario: A field is constrained with /^pattern/
    Given there is a field foo
    When foo should match regex /a{1,3}/
    And foo is required
    Then the following data should be generated:
      | foo |
      | a   |
      | aa  |
      | aaa |


