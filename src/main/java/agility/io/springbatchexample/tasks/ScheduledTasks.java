package agility.io.springbatchexample.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * You can schedule a method to be executed at a fixed interval by using fixedRate parameter in the @Scheduled annotation.
     */
    @Scheduled(fixedRate = 2000) // milisecond
    public void scheduleTaskWithFixedRate() {

        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }


    /**
     * You can execute a task with a fixed delay
     * between the completion of the last invocation and the start of the next, using fixedDelay parameter.
     * The fixedDelay parameter counts the delay after the completion of the last invocation.
     */
    @Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() {

        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            logger.error("Ran into an error {}", ex);
            throw new IllegalStateException(ex);
        }
    }


    /**
     * You can use initialDelay parameter with fixedRate and fixedDelay
     * to delay the first execution of the task with the specified number of milliseconds.
     * In the following example, the first execution of the task will be delayed by 5 seconds
     * and then it will be executed normally at a fixed interval of 2 seconds
     */
    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void scheduleTaskWithInitialDelay() {
        logger.info("Fixed Rate Task with Initial Delay :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }


    /**
     * Scheduling a Task using Cron Expression
     */
    @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }
}
