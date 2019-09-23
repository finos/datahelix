# (1075) ignoring these tests as only allowing latin character set for now but we will turn them back on when allow them
@ignore
Feature: Whilst including non-latin characters, user can specify that contains a specified regex

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo has type "string"

  Scenario: Running a 'containingRegex' request that includes special characters (non roman character maps: Hiragana) should be successful
    Given foo is containing regex /[あ-げ]{1}/
    And foo is of length 1
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

  @ignore #294 As a user I want to be able to configure the characters that can be emitted by the generator
  Scenario: Running a 'containingRegex' request that includes special characters (emoji) only should be successful
    Given foo is containing regex /[😁-😘]{1}/
    And foo is of length 1
    Then the following data should be generated:
      | foo  |
      | null |
      | "😁" |
      | "😂" |
      | "😃" |
      | "😄" |
      | "😅" |
      | "😆" |
      | "😉" |
      | "😊" |
      | "😋" |
      | "😌" |
      | "😍" |
      | "😏" |
      | "😒" |
      | "😓" |
      | "😔" |
      | "😖" |
      | "😘" |
