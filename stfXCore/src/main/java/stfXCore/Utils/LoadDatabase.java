package stfXCore.Utils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stfXCore.Models.Employee.Employee;
import stfXCore.Repositories.EmployeeRepository;

@Configuration
/**
 * Lombok annotation to autocreate an Slf4j-based LoggerFactory as log, allowing us to log these newly created "employees"
 */
@Slf4j
class LoadDatabase {

    /**
     * Spring Boot will run ALL CommandLineRunner beans once the application context is loaded
     */
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
        };
    }
}