Feature: To see a list of smartphones by state

  Scenario Outline: On Telcel page I would like to see a list of smartphones by state
    Given I am on telcel Page
    When I click on teléfonos y smart phones option
    And a modal window is opened
    Then I could select "<state>"
    And A list of cellphones is displayed
    Examples:
      | state          |
      | Ciudad         |
      | Aguascalientes |

  Scenario Outline: On Telcel page, I select a cellphone by state and validate phone information
    Given I am on telcel Page
    When I click on teléfonos y smart phones option
    And a modal window is opened
    Then I could select "<state>"
    And A list of cellphones is displayed
    And Select a <phone> of the list
    And validate the cellphone selected
    Examples:
      | state          | phone |
      | Ciudad         | 2     |
      | Aguascalientes | 1     |

