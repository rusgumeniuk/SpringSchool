package com.example.loaders;


import com.example.Group;
import com.example.repositories.GroupRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDBGroup {
    @Bean
    public CommandLineRunner initDbGroup(GroupRepository repository){
        return args ->{
            var checkList = repository.findAll();
            if(checkList.isEmpty()){
                log.info("Preloading group " + repository.save((new Group("IT-99"))));
                log.info("Preloading group " + repository.save(new Group("IK-11")));
            }
            else{
                log.info("Current rows in table groups: ");
                repository.findAll().forEach(group -> log.info(group.getId() + ":" + group.getTitle()));
            }
        };
    }
}
