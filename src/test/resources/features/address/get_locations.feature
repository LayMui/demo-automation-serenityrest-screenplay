@api
Feature: Get Locations 
In order to get a location based on unique userID
As a API consumer James
James wants to get the details an unique location

  Background:
    Given James is at the base url
  


    Scenario: Able to get the location with all the details using the yara userID
      When he requests location based on a unique userID
      Then he should get the location details
      
