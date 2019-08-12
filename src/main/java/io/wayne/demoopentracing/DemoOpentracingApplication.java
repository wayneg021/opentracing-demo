package io.wayne.demoopentracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;

@Slf4j
@SpringBootApplication
public class DemoOpentracingApplication implements ApplicationRunner {

    @Autowired
    CassandraOperations operations;

    public static void main(String[] args) {
        SpringApplication.run(DemoOpentracingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Person person = new Person("David", 17);
        operations.insert(person);

        log.info("Person Saved");

    }
}
