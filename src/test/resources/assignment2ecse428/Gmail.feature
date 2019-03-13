Feature: Gmail

  Scenario: Send an email with an image attachment to a single person
    Given a user is logged into their gmail
    When the compose button is selected
    And a valid recipient is inputted
    And the attach files button is selected
    And an image is selected
    And a subject is filled in the field
    And the send button is pressed
    Then the email will be sent

  Scenario: Send an email with an image attachment to multiple people
    Given a user is logged into their gmail
    When the compose button is selected
    And 2 valid recipients are given
    And the attach files button is selected
    And an image is selected
    And a subject is filled in the field
    And the send button is pressed
    Then the email will be sent

  Scenario: Send an email with several image attachments to a single person
    Given a user is logged into their gmail
    When the compose button is selected
    And a valid recipient is inputted
    And the attach files button is selected
    And an image is selected
    And the attach files button is selected again
    And a new image is selected
    And a subject is filled in the field
    And the send button is pressed
    Then the email will be sent

  Scenario: Send an email with several image attachments to several people
    Given a user is logged into their gmail
    When the compose button is selected
    And 2 valid recipients are given
    And the attach files button is selected
    And an image is selected
    And the attach files button is selected again
    And a new image is selected
    And a subject is filled in the field
    And the send button is pressed
    Then the email will be sent

  Scenario: Send an email with several image attachments to several people
    Given a user is logged into their gmail
    When the compose button is selected
    And the attach files button is selected
    And an image is selected
    And a subject is filled in the field
    And the send button is pressed
    Then a popup will appear warning the user