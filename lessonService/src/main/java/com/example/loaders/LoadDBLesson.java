package com.example.loaders;

import com.example.*;
import com.example.lessons.Lesson;
import com.example.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBLesson {
    @Bean
    public CommandLineRunner initDbLesson(
            LessonRepository repository,
            RoomRepository roomRepository,
            TeacherRepository teacherRepository,
            SubjectRepository subjectRepository,
            GroupRepository groupRepository){
        return args ->{
            List<Lesson> checkList = repository.findAll();
            if(checkList.isEmpty()){
                try{
                    Group group = groupRepository.findAll().get(0);
                    Teacher teacher = teacherRepository.findAll().get(0);
                    Room room = roomRepository.findAll().get(0);
                    Subject subject = subjectRepository.findAll().get(0);

                    log.info("Preloading lesson " + repository.save((new Lesson(room, subject, teacher, group))));
                }
                catch (Exception ex){
                    log.info("Ooooops, we can not create any lesson :c\n" + ex.getMessage());
                }
            }
            else{
                log.info("Current rows in table lesson: ");
                repository.findAll().forEach(lesson -> log.info("Lesson ID #" + lesson.getId()));
            }
        };
    }
}
