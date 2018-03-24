package agility.io.springbatchexample.configurations;

import agility.io.springbatchexample.Repositories.PersonRepository;
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
import org.springframework.batch.item.*;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort.Direction;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final PersonRepository personRepository;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, PersonRepository personRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.personRepository = personRepository;
    }

    /**
     * Reading data from CSV file
     */
    @Bean
    public FlatFileItemReader<Person> csvReader() {

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

    /**
     * Reading data from DB
     */
    @Bean
    public ItemReader<Person> dbReader() {

        logger.info("Reading from DB.........");

        RepositoryItemReader<Person> reader = new RepositoryItemReader<>();

        reader.setRepository(personRepository);
        reader.setMethodName("findAll");

        Map<String, Direction> sort = new HashMap<>();
        sort.put("id", Direction.ASC);

        reader.setSort(sort);

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
    @Qualifier(value = "csvJob")
    public Job createCsvJobForPerson(JobCompletionNotificationListener jobCompletionNotificationListener, @Qualifier(value = "csvStep") Step step1) {
        return jobBuilderFactory.get("createCsvJobForPerson")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    @Qualifier(value = "dbJob")
    public Job createDbJobForPerson(JobCompletionNotificationListener jobCompletionNotificationListener, @Qualifier(value = "dbStep") Step step1) {
        return jobBuilderFactory.get("createDbJobForPerson")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    @Qualifier(value = "csvStep")
    public Step createCsvStepForJobPerson(StepExecutionListener stepExecutionListener, ItemReadListener<Person> itemReadListener, ItemWriteListener<String> itemWriteListener) {
        return stepBuilderFactory.get("createCsvStepForJobPerson")
                .<Person, String> chunk(10)
                .reader(csvReader())
                .listener(itemReadListener)
                .processor(process())
                .writer(write())
                .listener(itemWriteListener)
                .listener(stepExecutionListener)
                .build();
    }

    @Bean
    @Qualifier(value = "dbStep")
    public Step createDbStepForJobPerson(StepExecutionListener stepExecutionListener, ItemReadListener<Person> itemReadListener, ItemWriteListener<String> itemWriteListener) {
        return stepBuilderFactory.get("createDbStepForJobPerson")
                .<Person, String> chunk(10)
                .reader(dbReader())
                .listener(itemReadListener)
                .processor(process())
                .writer(write())
                .listener(itemWriteListener)
                .listener(stepExecutionListener)
                .build();
    }
}
