package agility.io.springbatchexample.configurations.listeners;

import agility.io.springbatchexample.tasks.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomItemWriterListener implements ItemWriteListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Override
    public void beforeWrite(List<? extends String> list) {
        logger.info("ItemWriteListener - beforeWrite");
    }

    @Override
    public void afterWrite(List<? extends String> list) {
        logger.info("ItemWriteListener - afterWrite");
    }

    @Override
    public void onWriteError(Exception e, List<? extends String> list) {
        logger.info("ItemWriteListener - onWriteError: " + e.getMessage());
    }
}
