Feature: Testing if a field's value is in the set values

  Background:
    Given the generation strategy is full

    Scenario: User using inSet operator to constrain generator
      Given there is a field foo
      And foo is in set [ "X_092", "X_094" ]
      Then the following data should be generated:
      |  foo    |
      | "X_092" |
      | "X_094" |

