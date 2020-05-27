package io.swagger.IT;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "io.swagger.IT.steps",
        plugin = "pretty",
        strict = true
)

public class CucumberTest {
}
