package agility.io.springbatchexample.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobs {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final JobLauncher jobLauncher;

    private final Job job;

    public ScheduledJobs(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(fixedRate = 10000)
    public void runJob() {

        try {
            JobParameters jobParameters = new JobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
