package com.uw.steps;

import com.uw.App;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = App.class)
public class CucumberSpringConfiguration {
}