package com.example.loaders;

import com.example.Teacher;
import com.example.TeacherRank;
import com.example.repositories.TeacherRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBTeacher {
    @Bean
    public CommandLineRunner initDbTeacher(TeacherRepository repository){
        return args ->{
            List<Teacher> checkList = repository.findAll();
            if(checkList.isEmpty()){
                log.info("Preloading teacher " + repository.save((new Teacher("Michael Zgurovsky", TeacherRank.DOCTOR, "Not TK"))));
                log.info("Preloading teacher" + repository.save((new Teacher("Sergiy Telenik", TeacherRank.DOCTOR, "AUTS"))));
            }
            else{
                log.info("Current rows in table teacher: ");
                repository.findAll().forEach(teacher -> log.info("Teacher #" + teacher.getId() + " - " + teacher.getFullName()));
            }
        };
    }
}