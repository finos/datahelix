Feature: User can specify that a value is equalTo a required value

  Scenario:
    Given there is a field foo
    And foo is equal to "String24£"
    And foo is of type "string"
    And foo is not null
    Then the following data should be generated:
      | foo |
      |String24£|


  Scenario:
    Given there is a field foo
    When foo is equal to 23
    And foo is of type "numeric"
    And foo is not null
    Then the following data should be generated:
      | foo |
      |23|