Feature: Book search
  To allow a customer to find his favourite books quickly, the library must offer multiple ways to search for a book.

  Scenario: Search books by publication year
    Given I have a list of books
      | Title              | Author          | Date       |
      | One good book      | Anonymous       | 2013-03-14 |
      | Some other book    | Tim Tomson      | 2014-08-28 |
      | How to cook a dino | Fred Flintstone | 2012-01-01 |
    When the customer searches for books published between 2013 and 2014
    Then 2 books should have been found
    And Book 1 should have the title 'Some other book'
    And Book 2 should have the title 'One good book'