package demo;


import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;


@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty"},
    strict = true,
    snippets = CAMELCASE,
    features = "classpath:features")
public class CucumberTestSuite {}
