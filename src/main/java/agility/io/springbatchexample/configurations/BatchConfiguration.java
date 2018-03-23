package agility.io.springbatchexample.configurations;

import agility.io.springbatchexample.configurations.listeners.JobCompletionNotificationListener;
import agility.io.springbatchexample.domains.Person;
import agility.io.springbatchexample.tasks.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<Person> reader() {

        logger.info("Reading.........");

        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Person>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] {
                                "firstName",
                                "lastName"
                        });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                    {
                        setTargetType(Person.class);
                    }
                });
            }
        });

        return reader;
    }

    @Bean
    public ItemProcessor<Person, String> process() {

        logger.info("Processing.........");

        return person -> {

            Person newPerson = new Person();
            newPerson.setFirstName(person.getFirstName().toUpperCase());
            newPerson.setLastName(person.getLastName().toUpperCase());

            return newPerson.toString();
        };
    }

    @Bean
    public ItemWriter<String> write() {

        logger.info("Writing.........");

        return list -> {
            for (String s : list) {
                logger.info(s);
            }
        };
    }

    @Bean
    public Job createFirstJobForPerson(JobCompletionNotificationListener jobCompletionNotificationListener, Step step1) {
        return jobBuilderFactory.get("createFirstJobForPerson")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .listener(jobCompletionNotificationListener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step createFirstStepForFirstJobPerson(StepExecutionListener stepExecutionListener, ItemReadListener<Person> itemReadListener, ItemWriteListener<String> itemWriteListener) {
        return stepBuilderFactory.get("createFirstStepForFirstJobPerson")
                .<Person, String> chunk(10)
                .reader(reader())
                .listener(itemReadListener)
                .processor(process())
                .writer(write())
                .listener(itemWriteListener)
                .listener(stepExecutionListener)
                .build();
    }
}
