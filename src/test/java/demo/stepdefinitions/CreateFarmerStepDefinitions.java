package demo.stepdefinitions;

import demo.dto.FarmerData;
import demo.dto.FarmerDto;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class CreateFarmerStepDefinitions {
    private String theRestApiBaseUrl;
    EnvironmentVariables environmentVariables;
    private final Logger log = LoggerFactory.getLogger(CreateFarmerStepDefinitions.class);
    Actor james;

    @Before()
    public void setTheRestApiBaseUrl() {
        theRestApiBaseUrl = environmentVariables.optionalProperty("restapi.farmerBaseurl")
                .orElse("https://farmer-service.stage.apac.yaradigitallabs.io");

        log.info("theRestApiBaseUrl: " + theRestApiBaseUrl);
        james = Actor.named("James the Api  user").whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @After()
    public void tearDown() {

    }

    @Given("James is at the farmer services url")
    public void jamesIsAtTheFarmerServicesUrl() {

    }
    @When("he create a farmer using a unique yara UserID")
    public void heCreateAFarmerUsingAUniqueYaraUserID() {

        FarmerData farmerData = new FarmerData("english",
                "https://www.photourlsampletest.com/photourl.png",
                "https://www.nationalIdsampletest.com/nationalId",
                "Haryana",
                "Sector 31",
                "Gurugram",
                "street name",
                "title",
                "2017",
                17.385, 78.4867, 500060);

        FarmerDto farmerDto = new FarmerDto(UUID.randomUUID().toString(),
                "first name",
                "last name",
                Double.toString(Math.floor(Math.random() * Math.pow(10,8))),
                Double.toString(Math.floor(Math.random() * Math.pow(10,10))),
                "FEMALE", "2021-03-26", "India",
                Double.toString(Math.floor(Math.random() * Math.pow(10,8))) + "@email.com",
                farmerData);

        james.attemptsTo(
                Post.to("/v1/farmer-profile/farmers")
                        .with(request -> {
                            request.relaxedHTTPSValidation();
                            request.body(farmerDto);
                            request.header("Content-Type", "application/json");
                            request.header("accept", "application/json");
                            request.header("x-api-key", "ce58d8e8-1c8a-447b-bdb1-88464469cb73").log().all();
                            return request;
                        }));
    }

    @Then("he should able to get the details of the new farmer profile")
    public void heShouldAbleToGetTheDetailsOfTheNewFarmerProfile() {
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(201)));

        log.info("RESPONSE: " + SerenityRest.then().extract().body().jsonPath().prettyPrint());
    }


}
