package com.example.loaders;

import com.example.ControlType;
import com.example.Subject;
import com.example.repositories.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBSubject {
    @Bean
    public CommandLineRunner initDbSubject(SubjectRepository repository){
        return args ->{
            List<Subject> checkList = repository.findAll();
            if(checkList.isEmpty()){
                log.info("Preloading subject " + repository.save((new Subject("SMRTPZ", ControlType.EXAM))));
                log.info("Preloading subject" + repository.save((new Subject("Law", ControlType.DIFFTEST))));
            }
            else{
                log.info("Current rows in table subject: ");
                repository.findAll().forEach(subject -> log.info("Subject #" + subject.getId() + " - is a " + subject.getTitle()));
            }
        };
    }
}