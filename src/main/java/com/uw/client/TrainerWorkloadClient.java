package com.uw.client;

import com.uw.config.FeignClientConfig;
import com.uw.dto.TrainingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "trainer-workload-service", configuration = FeignClientConfig.class)
public interface TrainerWorkloadClient {

      @PostMapping(value = "/trainer-workload/training-request", consumes = "application/json")
      ResponseEntity<String> trainingRequest (@RequestBody TrainingRequest trainingRequest ,
                                         @RequestHeader("Authorization") String token);

      @GetMapping(value = "/trainer-workload/{username}/{year}/{month}", consumes = "application/json")
      int getMonthlyHours (
              @PathVariable(name = "username") String username,
              @PathVariable(name = "year") int year,
              @PathVariable(name = "month") int month,
              @RequestHeader("Authorization") String token);

}
