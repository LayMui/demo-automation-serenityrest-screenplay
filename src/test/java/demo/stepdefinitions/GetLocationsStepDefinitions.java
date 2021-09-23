package demo.stepdefinitions;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class GetLocationsStepDefinitions {

        private String theRestApiBaseUrl;
        EnvironmentVariables environmentVariables;
        private final Logger log = LoggerFactory.getLogger(GetLocationsStepDefinitions.class);
        Actor james;

        @Before()
        public void setTheRestApiBaseUrl() {
                theRestApiBaseUrl = environmentVariables.optionalProperty("restapi.addressBaseurl")
                                .orElse("https://address-service.stage.apac.yaradigitallabs.io");

                log.info("theRestApiBaseUrl: " + theRestApiBaseUrl);
                james = Actor.named("James the mobile user").whoCan(CallAnApi.at(theRestApiBaseUrl));
        }

        @After()
        public void tearDown() {

        }

        @Given("^(?:.*) is at the base url")
        public void jamesIsAtTheBaseUrl() {
                // Already at the base url
        }

        @When("^(?:.*) requests location based on a unique userID$")
        public void heRequestsLocationBasedOnAUniqueUserID() {
                james.attemptsTo(
                        Get.resource("/v1/address/locations/ca2bd511-9412-4f2c-8db7-9bd5b46ca9b0")
                                .with(request -> request.relaxedHTTPSValidation())
                                .with(request -> request.header("Accept", "Application/json"))
                                .with(request -> request.header("x-api-key", "ce58d8e8-1c8a-447b-bdb1-88464469cb73")));
        }

        @Then("^(?:.*) should get the location details$")
        public void heShouldGetTheLocationDetails() {
                james.should(
                        seeThatResponse(
                                "the expected response should be returned", response -> response.statusCode(200)));

                log.info("RESPONSE: " + SerenityRest.then().extract().body().jsonPath().prettyPrint());
        }


}