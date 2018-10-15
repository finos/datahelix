Feature: User can specify that a value is equalTo a required value

  Scenario: test1
    Given there is a field foo
    And foo is equal to "String24£"
    And foo is of type "string"
    Then the following data should be generated:
      | foo |
      |String24£|


  Scenario: test2
    Given there is a field foo
    When foo is equal to 23
    And foo is of type "numeric"
    Then the following data should be generated:
      | foo |
      | 23  |

