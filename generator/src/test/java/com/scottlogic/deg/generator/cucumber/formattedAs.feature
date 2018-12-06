Feature: User can specify that a value is so formatted

  Background:
    Given the generation strategy is full
    And there is a field foo



  Scenario Outline: Running a valid 'formattedAs' request should be successful
    Given foo is in set:
      | <input> |
    And foo is formatted as <format>
    Then the following data should be generated:
      | foo        |
      | <expected> |
    Examples:
      | input                    | format       | expected                       |
#      | 1.0                      | "%a"         | "0x1.0p0"                      | no way to specify float over double
#      | 1.5                      | "%a"         | "0x1.8p0"                      | no way to specify float over double
      | null                     | "%a"         | null                           |
      | 1                        | "%b"         | true                           |
      | "1"                      | "%b"         | true                           |
      | 2018-10-10T00:00:00.000  | "%b"         | true                           |
#      | null                     | "%b"         | true                           | this should be true
      |                          | "%b"         | true                           |
      | 32                       | "%c"         | " "                            |
      | 33                       | "%c"         | "!"                            |
      | 34                       | "%c"         | """                            |
      | 40                       | "%c"         | "("                            |
      | 46                       | "%c"         | "."                            |
      | 48                       | "%c"         | "0"                            |
      | 59                       | "%c"         | ";"                            |
      | 65                       | "%c"         | "A"                            |
      | 96                       | "%c"         | "`"                            |
      | 97                       | "%c"         | "a"                            |
      | 123                      | "%c"         | "{"                            |
      | 124                      | "%c"         | "\|"                           |
      | null                     | "%c"         | null                           |
      | 0                        | "%d"         | "0"                            |
      | 1                        | "%d"         | "1"                            |
      | 9223372036854775808      | "%d"         | "9223372036854775808"          |
      | -9223372036854775809     | "%d"         | "-9223372036854775809"         |
      | null                     | "%d"         | null                           |
      | 1                        | "%20d"       | "                   1"         |
      | -1                       | "%20d"       | "                  -1"         |
      | 11                       | "%20d"       | "                  11"         |
      | 1111111111111111111      | "%20d"       | " 1111111111111111111"         |
      | 11111111111111111111     | "%20d"       | "11111111111111111111"         |
      | 111111111111111111111    | "%20d"       | "111111111111111111111"        |
      | 1                        | "%-20d"      | "1                   "         |
      | -1                       | "%-20d"      | "-1                  "         |
      | 11                       | "%-20d"      | "11                  "         |
      | 1111111111111111111      | "%-20d"      | "1111111111111111111 "         |
      | 11111111111111111111     | "%-20d"      | "11111111111111111111"         |
      | 111111111111111111111    | "%-20d"      | "111111111111111111111"        |
      | 1                        | "%020d"      | "00000000000000000001"         |
      | -1                       | "%020d"      | "-0000000000000000001"         |
      | 11                       | "%020d"      | "00000000000000000011"         |
      | 1111111111111111111      | "%020d"      | "01111111111111111111"         |
      | 11111111111111111111     | "%020d"      | "11111111111111111111"         |
      | 111111111111111111111    | "%020d"      | "111111111111111111111"        |
      | 1                        | "\|%+20d\|"  | "\|                  +1\|"     |
      | -1                       | "\|%+20d\|"  | "\|                  -1\|"     |
      | 11                       | "\|%+20d\|"  | "\|                 +11\|"     |
      | 1111111111111111111      | "\|%+20d\|"  | "\|+1111111111111111111\|"     |
      | 11111111111111111111     | "\|%+20d\|"  | "\|+11111111111111111111\|"    |
      | 111111111111111111111    | "\|%+20d\|"  | "\|+111111111111111111111\|"   |
      | 1                        | "% d"        | " 1"                           |
      | 11                       | "% d"        | " 11"                          |
      | 1111111111111111111      | "% d"        | " 1111111111111111111"         |
      | 11111111111111111111     | "% d"        | " 11111111111111111111"        |
      | 111111111111111111111    | "% d"        | " 111111111111111111111"       |
      | 1                        | "%,d"        | "1"                            |
      | -1                       | "%,d"        | "-1"                           |
      | 11                       | "%,d"        | "11"                           |
      | 111                      | "%,d"        | "111"                          |
      | 1111                     | "%,d"        | "1,111"                        |
      | 1111111111111111111      | "%,d"        | "1,111,111,111,111,111,111"    |
      | 1                        | "%(d"        | "1"                            |
      | 11                       | "%(d"        | "11"                           |
      | 1111111111111111111      | "%(d"        | "1111111111111111111"          |
      | 11111111111111111111     | "%(d"        | "11111111111111111111"         |
      | 111111111111111111111    | "%(d"        | "111111111111111111111"        |
      | -1                       | "%(d"        | "(1)"                          |
      | -11                      | "%(d"        | "(11)"                         |
      | -1111111111111111111     | "%(d"        | "(1111111111111111111)"        |
      | -11111111111111111111    | "%(d"        | "(11111111111111111111)"       |
      | -111111111111111111111   | "%(d"        | "(111111111111111111111)"      |
      | 1.0                      | "%e"         | "1.000000e+00"                 |
      | -1.0                     | "%e"         | "-1.000000e+00"                |
      | 100.0                    | "%e"         | "1.000000e+02"                 |
      | 123456789.123456789      | "%e"         | "1.234568e+08"                 |
      | null                     | "%e"         | null                           |
      | 1.0                      | "%f"         | "1.000000"                     |
      | -1.0                     | "%f"         | "-1.000000"                    |
      | 1.1                      | "%f"         | "1.100000"                     |
      | 123456789.123456789      | "%f"         | "123456789.123457"             |
      | null                     | "%f"         | null                           |
      | 1.0                      | "%g"         | "1.00000"                      |
      | -1.0                     | "%g"         | "-1.00000"                     |
      | 1.1                      | "%g"         | "1.10000"                      |
      | 123456789.123456789      | "%g"         | "1.23457e+08"                  |
      | null                     | "%g"         | null                           |
      | ""                       | "%h"         | "0"                            |
#      |                          | "%n"         | "\n"                           | struggling to display the correct kind of newline
#      | 1                        | "%n"         | "\n"                           | struggling to display the correct kind of newline
#      | "1"                      | "%n"         | "\n"                           | struggling to display the correct kind of newline
#      | 2018-10-10T00:00:00.000  | "%n"         | "\n"                           | struggling to display the correct kind of newline
#      | null                     | "%n"         | "\n"                           | struggling to display the correct kind of newline
      | 0                        | "%o"         | "0"                            |
      | 1                        | "%o"         | "1"                            |
      | 123456789                | "%o"         | "726746425"                    |
      | -123456789               | "%o"         | "37051031353"                  |
      | null                     | "%o"         | null                           |
      |                          | "%s"         | ""                             |
      | 1                        | "%s"         | "1"                            |
      | "1"                      | "%s"         | "1"                            |
      | 2018-10-10T00:00:00.000  | "%s"         | "2018-10-10T00:00"             |
      | 2018-10-10T00:00:10.000  | "%s"         | "2018-10-10T00:00:10"          |
      | 2018-10-10T00:00:01.000  | "%s"         | "2018-10-10T00:00:01"          |
      | 2018-10-10T00:00:59.100  | "%s"         | "2018-10-10T00:00:59.100"      |
      | 2018-10-10T00:00:59.001  | "%s"         | "2018-10-10T00:00:59.001"      |
      | null                     | "%s"         | null                           |
      |                          | "%10s"       | "          "                   |
      | 1                        | "%10s"       | "         1"                   |
      | 11111111111              | "%10s"       | "11111111111"                  |
      | "1"                      | "%10s"       | "         1"                   |
      | "11111111111"            | "%10s"       | "11111111111"                  |
      | 2018-10-10T00:00:00.000  | "%10s"       | "2018-10-10T00:00"             |
      | 2018-10-10T00:00:10.000  | "%10s"       | "2018-10-10T00:00:10"          |
      | 2018-10-10T00:00:01.000  | "%10s"       | "2018-10-10T00:00:01"          |
      | 2018-10-10T00:00:59.100  | "%10s"       | "2018-10-10T00:00:59.100"      |
      | 2018-10-10T00:00:59.001  | "%10s"       | "2018-10-10T00:00:59.001"      |
      |                          | "%-10s"      | "          "                   |
      | 1                        | "%-10s"      | "1         "                   |
      | 11111111111              | "%-10s"      | "11111111111"                  |
      | "1"                      | "%-10s"      | "1         "                   |
      | "11111111111"            | "%-10s"      | "11111111111"                  |
      | 2018-10-10T00:00:00.000  | "%-10s"      | "2018-10-10T00:00"             |
      | 2018-10-10T00:00:10.000  | "%-10s"      | "2018-10-10T00:00:10"          |
      | 2018-10-10T00:00:01.000  | "%-10s"      | "2018-10-10T00:00:01"          |
      | 2018-10-10T00:00:59.100  | "%-10s"      | "2018-10-10T00:00:59.100"      |
      | 2018-10-10T00:00:59.001  | "%-10s"      | "2018-10-10T00:00:59.001"      |
      | null                     | "%-10s"      | null                           |
      |                          | "%.5s"       | ""                             |
      | 1                        | "%.5s"       | "1"                            |
      | 11111111111              | "%.5s"       | "11111"                        |
      | "1"                      | "%.5s"       | "1"                            |
      | "11111111111"            | "%.5s"       | "11111"                        |
      | 2018-10-10T00:00:00.000  | "%.5s"       | "2018-"                        |
      | 2018-10-10T00:00:10.000  | "%.5s"       | "2018-"                        |
      | 2018-10-10T00:00:01.000  | "%.5s"       | "2018-"                        |
      | 2018-10-10T00:00:59.100  | "%.5s"       | "2018-"                        |
      | 2018-10-10T00:00:59.001  | "%.5s"       | "2018-"                        |
      | null                     | "%.5s"       | null                           |
      | null                     | "%10.5s"     | null                           |
      |                          | "%10.5s"     | "          "                   |
      | 1                        | "%10.5s"     | "         1"                   |
      | 12345678901              | "%10.5s"     | "     12345"                   |
      | "1"                      | "%10.5s"     | "         1"                   |
      | "12345678901"            | "%10.5s"     | "     12345"                   |
      | 2018-10-10T00:00:00.000  | "%10.5s"     | "     2018-"                   |
      | 2018-10-10T00:00:10.000  | "%10.5s"     | "     2018-"                   |
      | 2018-10-10T00:00:01.000  | "%10.5s"     | "     2018-"                   |
      | 2018-10-10T00:00:59.100  | "%10.5s"     | "     2018-"                   |
      | 2018-10-10T00:00:59.001  | "%10.5s"     | "     2018-"                   |
      | null                     | "%10.5s"     | null                           |
      | null                     | "%t"         | null                           |
      | 0                        | "%x"         | "0"                            |
      | 1                        | "%x"         | "1"                            |
      | 123456789                | "%x"         | "75bcd15"                      |
      | -123456789               | "%x"         | "f8a432eb"                     |
      | null                     | "%x"         | null                           |
      | 2018-12-01T16:17:18.199  | "%tA"        | "Saturday"                     |
      | 2018-12-01T16:17:18.199  | "%ta"        | "Sat"                          |
      | 2018-12-01T16:17:18.199  | "%tB"        | "December"                     |
      | 2018-12-01T16:17:18.199  | "%tb"        | "Dec"                          |
      | 2018-12-01T16:17:18.199  | "%tC"        | "20"                           |
#      | 2018-12-01T16:17:18.199Z | "%tc"        | "Sat Dec 11 16:17:18 UTC 2018" | requires timezone information
      | 2018-12-01T16:17:18.199  | "%tD"        | "12/01/18"                     |
      | 2018-12-01T16:17:18.199  | "%td"        | "01"                           |
      | 2018-12-01T16:17:18.199  | "%te"        | "1"                            |
      | 2018-12-01T16:17:18.199  | "%tF"        | "2018-12-01"                   |
      | 2018-12-01T16:17:18.199  | "%tH"        | "16"                           |
      | 2018-12-01T16:17:18.199  | "%th"        | "Dec"                          |
      | 2018-12-01T16:17:18.199  | "%tI"        | "04"                           |
      | 2018-12-01T16:17:18.199  | "%tj"        | "335"                          |
      | 2018-12-01T09:17:18.199  | "%tk"        | "9"                            |
      | 2018-12-01T16:17:18.199  | "%tl"        | "4"                            |
      | 2018-12-01T16:07:18.199  | "%tM"        | "07"                           |
      | 2018-02-01T16:17:18.199  | "%tm"        | "02"                           |
      | 2018-02-01T16:17:18.099  | "%tN"        | "099000000"                    |
      | 2018-02-01T16:17:18.199  | "%tp"        | "pm"                           |
#      | 2018-02-01T16:17:18.199  | "%tQ"        | "02"                           | requires timezone information
      | 2018-02-01T16:17:18.199  | "%tR"        | "16:17"                        |
#      | 2018-02-01T16:17:18.199  | "%tr"        | "04:17:18 pm"                  | pm is being capitalised
      | 2018-02-01T16:17:08.199  | "%tS"        | "08"                           |
#      | 2018-02-01T16:17:18.199  | "%ts"        | "02"                           | requires timezone information
      | 2018-02-01T16:17:18.199  | "%tT"        | "16:17:18"                     |
      | 2018-02-01T16:17:08.199  | "%tY"        | "2018"                         |
      | 2018-02-01T16:17:08.199  | "%ty"        | "18"                           |
#      | 2018-02-01T16:17:08.199  | "%tZ"        | "08"                           | requires timezone information
#      | 2018-02-01T16:17:08.199  | "%tz"        | "08"                           | requires timezone information
