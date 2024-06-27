package com.uw.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UserMetrics {

    private final Counter userRegistrationCounter;

    public UserMetrics(MeterRegistry registry){
    userRegistrationCounter = Counter.builder("user_registered_counter")
            .description("Number of users registered")
            .register(registry);
    }

    public void incrementUserRegistrationCount() {
        userRegistrationCounter.increment();
    }

}
