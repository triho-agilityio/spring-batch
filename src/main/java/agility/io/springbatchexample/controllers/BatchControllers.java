package agility.io.springbatchexample.controllers;

import agility.io.springbatchexample.Repositories.PersonRepository;
import agility.io.springbatchexample.domains.Person;
import agility.io.springbatchexample.tasks.ScheduledJobs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatchControllers {

    private final ScheduledJobs scheduledJobs;

    private final PersonRepository personRepository;

    public BatchControllers(ScheduledJobs scheduledJobs, PersonRepository personRepository) {
        this.scheduledJobs = scheduledJobs;
        this.personRepository = personRepository;
    }

    @GetMapping(value = "/person-jobs")
    public void runPersonJob() {

        scheduledJobs.runJob("csv");
    }

    @PostMapping(value = "/person-jobs")
    public void runPersonJob(@RequestBody List<Person> people) {

        // Save to database
        personRepository.save(people);

        // Call run Person-job
        scheduledJobs.runJob("db");
    }
}
