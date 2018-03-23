package agility.io.springbatchexample.configurations.listeners;

import agility.io.springbatchexample.tasks.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            logger.info("===== !!! JOB FINISHED! =====");
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        logger.info("===== JOB START =====");

        super.beforeJob(jobExecution);
    }
}
