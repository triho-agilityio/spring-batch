package agility.io.springbatchexample.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledJobs {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final JobLauncher jobLauncher;

    private final Job job;

    public ScheduledJobs(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void runJob() {

        try {
            Map<String, JobParameter> jobParameterMap = new HashMap<>();
            jobParameterMap.put("person", new JobParameter("person"));
            JobParameters jobParameters = new JobParameters(jobParameterMap);


            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
