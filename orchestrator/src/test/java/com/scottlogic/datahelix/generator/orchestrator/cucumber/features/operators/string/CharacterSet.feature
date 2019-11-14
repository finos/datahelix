# Copyright 2019 Scott Logic Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
Feature: Strings will be generated using characters from only latin characters

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "string"

  Scenario: Running a 'matchingRegex' request that includes roman alphabet lowercase chars (a-z) only should be successful
    Given foo is matching regex /./
    Then the following data should be generated:
      | foo  |
      | " "  |
      | "!"  |
      | """  |
      | "#"  |
      | "$"  |
      | "%"  |
      | "&"  |
      | "'"  |
      | "("  |
      | ")"  |
      | "*"  |
      | "+"  |
      | ","  |
      | "-"  |
      | "."  |
      | "/"  |
      | "0"  |
      | "1"  |
      | "2"  |
      | "3"  |
      | "4"  |
      | "5"  |
      | "6"  |
      | "7"  |
      | "8"  |
      | "9"  |
      | ":"  |
      | ";"  |
      | "<"  |
      | "="  |
      | ">"  |
      | "?"  |
      | "@"  |
      | "A"  |
      | "B"  |
      | "C"  |
      | "D"  |
      | "E"  |
      | "F"  |
      | "G"  |
      | "H"  |
      | "I"  |
      | "J"  |
      | "K"  |
      | "L"  |
      | "M"  |
      | "N"  |
      | "O"  |
      | "P"  |
      | "Q"  |
      | "R"  |
      | "S"  |
      | "T"  |
      | "U"  |
      | "V"  |
      | "W"  |
      | "X"  |
      | "Y"  |
      | "Z"  |
      | "["  |
      | "\"  |
      | "]"  |
      | "^"  |
      | "_"  |
      | "`"  |
      | "a"  |
      | "b"  |
      | "c"  |
      | "d"  |
      | "e"  |
      | "f"  |
      | "g"  |
      | "h"  |
      | "i"  |
      | "j"  |
      | "k"  |
      | "l"  |
      | "m"  |
      | "n"  |
      | "o"  |
      | "p"  |
      | "q"  |
      | "r"  |
      | "s"  |
      | "t"  |
      | "u"  |
      | "v"  |
      | "w"  |
      | "x"  |
      | "y"  |
      | "z"  |
      | "{"  |
      | "\|" |
      | "}"  |
      | "~"  |
