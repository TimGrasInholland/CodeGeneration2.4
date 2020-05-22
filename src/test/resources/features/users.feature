Feature: Users tests

  Scenario: Retrieve all users is OK
    When I retrieve all users
    Then I get http status users 200