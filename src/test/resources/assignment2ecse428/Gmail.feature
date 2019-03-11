Feature: Gmail

  Scenario: Send an email with an image attachment to a single person
    Given a user is logged into their gmail
    When the compose button is selected
    And a valid recipient is given
    And the attach files button is selected
    And an image is selected
    And a subject is filled in the field
    And the send button is pressed
    Then the email will be sent