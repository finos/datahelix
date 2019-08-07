# (1075) ignoring these tests as only allowing latin character set for now but we will turn them back on when allow them
@ignore
Feature: Whilst including non-latin characters, user can specify that a value either matches or contains a specified regex

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is of type "string"

  Scenario: Running a 'matchingRegex' request that includes special characters (non roman character maps: Hiragana) should be successful
    Given foo is matching regex /[あ-げ]{1}/
    Then the following data should be generated:
      | foo  |
      | null |
      | "あ"  |
      | "ぃ"  |
      | "い"  |
      | "ぅ"  |
      | "う"  |
      | "ぇ"  |
      | "え"  |
      | "ぉ"  |
      | "お"  |
      | "か"  |
      | "が"  |
      | "き"  |
      | "ぎ"  |
      | "く"  |
      | "ぐ"  |
      | "け"  |
      | "げ"  |

  Scenario: Running a 'matchingRegex' request that includes special characters (emoji) only should be successful
    Given foo is matching regex /[😁-😘]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

