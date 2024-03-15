Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Substraction
    When I substract 7 to 2
    Then the result is 5

  Scenario: Multiplication
    When I multiply 4 by 3
    Then the result is 12

  Scenario: Division (integers)
    When I divide 4 by 2
    Then the result is 2

  Scenario: Division (double)
    When I divide 4 by 3
    Then the result is 1.3333333333333333

  Scenario: Division by zero
    When I divide 2 by 0
    Then the result is infinity

  Scenario Outline: Several additions
    When I add <a> and <b>
    Then the result is <c>

    Examples: Single digits
      | a | b | c  |
      | 1 | 2 | 3  |
      | 3 | 7 | 10 |