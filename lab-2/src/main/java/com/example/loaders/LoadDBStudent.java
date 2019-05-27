package com.example.loaders;


import com.example.Male;
import com.example.Student;
import com.example.repositories.StudentRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDBStudent {

    @Bean
    public CommandLineRunner initDbStudent(StudentRepository repository){
        return args ->{
            var checkList = repository.findAll();
            if(checkList.isEmpty()){
                log.info("Preloading student " + repository.save((new Student("Ruslan", 19, Male.MALE, "Vyshneve"))));
                log.info("Preloading student " + repository.save(new Student("Olya", 20, Male.FEMALE, "Kyiv")));
            }
            else{
                log.info("Current rows in table students: ");
                repository.findAll().forEach(student -> log.info(student.getId() + ":" + student.getName() + "from " + student.getGroup()));
            }
        };
    }
}
