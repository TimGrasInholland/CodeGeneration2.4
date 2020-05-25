Feature: Security tests

  Scenario: Login with a correct username and password
    When I login with username "Inholland-Bank" and password "Welcome567?"
    Then I get http status security 200

  Scenario: Logout from the application
    When I logout
    Then I get http status security 200

  Scenario: Retrieve a sessionToken by authKey is OK
    When I retrieve a sessionToken by authKey is "testEmployee"
    Then I get http status security 200