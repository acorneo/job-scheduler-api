package me.acorneo.jobschedulerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobSchedulerApiApplication {

    static void main(String[] args) {
        SpringApplication.run(JobSchedulerApiApplication.class, args);
    }

}
