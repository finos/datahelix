Feature: User can specify that a field must be a type of name

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: Basic cucumber test to check names work
    Given foo is of type "firstname"
    And the generator can generate at most 5 rows
    Then some data should be generated
