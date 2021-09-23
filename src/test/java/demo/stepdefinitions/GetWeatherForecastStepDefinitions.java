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

public class GetWeatherForecastStepDefinitions {

    private String theRestApiBaseUrl;
    EnvironmentVariables environmentVariables;
    private final Logger log = LoggerFactory.getLogger(GetWeatherForecastStepDefinitions.class);
    Actor james;

    @Before()
    public void setTheRestApiBaseUrl() {
        theRestApiBaseUrl = environmentVariables.optionalProperty("restapi.weatherBaseurl")
                .orElse("https://weather-service.stage.apac.yaradigitallabs.io");

        log.info("theRestApiBaseUrl: " + theRestApiBaseUrl);
        james = Actor.named("James the mobile user").whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @After()
    public void tearDown() {

    }

    @Given("^(?:.*) is at the weather services url")
    public void jamesIsAtTheWeatherServicesUrl() {

    }

    @When("he requests weather forecast hourly based on geocode {string}")
    public void heRequestsWeatherForecastHourlyBasedOnGeocode(String geocode) {
        james.attemptsTo(
                Get.resource("/v1/weather/forecast/hourly?geocode={geocode}")
                        .with(request -> request.relaxedHTTPSValidation())
                        .with(request -> request.pathParam("geocode", geocode))
                        .with(request -> request.header("Accept", "Application/json"))
                        .with(request -> request.header("x-api-key", "ce58d8e8-1c8a-447b-bdb1-88464469cb73")));


    }
    @Then("he should get the weather forecast details")
    public void heShouldGetTheWeatherForecastDetails() {
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(200)));

        log.info("RESPONSE: " + SerenityRest.then().extract().body().jsonPath().prettyPrint());

    }

}
