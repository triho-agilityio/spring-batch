package agility.io.springbatchexample.controllers;

import agility.io.springbatchexample.tasks.ScheduledJobs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchControllers {

    private final ScheduledJobs scheduledJobs;

    public BatchControllers(ScheduledJobs scheduledJobs) {
        this.scheduledJobs = scheduledJobs;
    }

    @GetMapping(value = "/person-jobs")
    public void runPersonJob() {
        scheduledJobs.runJob();
    }
}
