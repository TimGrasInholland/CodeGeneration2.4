Feature: Security tests

  Scenario: Login with a correct username and password
    When I login with username "SjaakMaster" and password "Test123!"
    Then I get http status security 200

  Scenario: Logout from the application
    When I logout with authKey is "38ce48da-a0da-11ea-bb37-0242ac130002"
    Then I get http status security 200

  Scenario: Retrieve a sessionToken by authKey is OK
    When I retrieve a sessionToken by authKey is "40e7a688-a0da-11ea-bb37-0242ac130002"
    Then I get http status security 200