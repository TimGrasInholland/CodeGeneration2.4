package io.swagger.IT;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "io.swagger.IT.steps",
        plugin = "pretty",
        strict = true
)
public class CucumberIT {
}
