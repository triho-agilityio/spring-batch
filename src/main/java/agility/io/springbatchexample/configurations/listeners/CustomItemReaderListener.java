package agility.io.springbatchexample.configurations.listeners;

import agility.io.springbatchexample.domains.Person;
import agility.io.springbatchexample.tasks.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
public class CustomItemReaderListener implements ItemReadListener<Person> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Override
    public void beforeRead() {
        logger.info("ItemReadListener - beforeRead");
    }

    @Override
    public void afterRead(Person person) {
        logger.info("ItemReadListener - afterRead");
    }

    @Override
    public void onReadError(Exception e) {
        logger.info("ItemReadListener - onReadError: " + e.getMessage());
    }
}
