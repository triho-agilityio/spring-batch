package agility.io.springbatchexample.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledJobs {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final JobLauncher jobLauncher;

    @Qualifier(value = "csvJob")
    private final Job csvJob;

    @Qualifier(value = "dbJob")
    private final Job dbJob;

    public ScheduledJobs(JobLauncher jobLauncher, @Qualifier(value = "csvJob") Job csvJob, @Qualifier(value = "dbJob") Job dbJob) {
        this.jobLauncher = jobLauncher;
        this.csvJob = csvJob;
        this.dbJob = dbJob;
    }

    public void runJob(String jobParameter) {

        try {
            Map<String, JobParameter> jobParameterMap = new HashMap<>();
            jobParameterMap.put("person", new JobParameter(jobParameter));
            JobParameters jobParameters = new JobParameters(jobParameterMap);

            if (jobParameter.equals("csv"))
                jobLauncher.run(csvJob, jobParameters);

            if (jobParameter.equals("db"))
                jobLauncher.run(dbJob, jobParameters);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
