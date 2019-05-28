package com.example;

import com.example.messages.GroupMessage;
import com.example.messages.StudentMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.tracing.dtrace.ProviderAttributes;
import lombok.experimental.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@EnableDiscoveryClient
public class MsgController {

    @Autowired
    private MsgProducer producer;
    private static Logger log = LoggerFactory.getLogger(EurekaClientLab2Application.class);
    private final RestTemplate restTemplate;

    @Autowired
    public MsgController(RestTemplateBuilder restTemplateBuilder,
                         RestTemplateResponseErrorHandler myResponseErrorHandler
    ) {

        this.restTemplate = restTemplateBuilder
                .errorHandler(myResponseErrorHandler)
                .build();
    }

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private Environment env;

    /* REFRESH */
    @PostMapping(value = "/refreshing", produces = "application/json; charset=UTF-8")
    public String checkRefresh() throws JsonProcessingException{
        return refresh() + getPropertiesClient();
    }
    @PostMapping(value = "/actuator/bus-refresh", produces = "application/json; charset=UTF-8")
    public String refresh()
    {
        return "Refreshed";
    }
    @GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
    public String getPropertiesClient() throws JsonProcessingException{
        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
        for (String propertyName : bootstrapProperties.getPropertyNames()) {
            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(props);
    }

    /* INSTANCES */
    @RequestMapping(value = "/instances")
    public String getInstancesRun(){
        ServiceInstance instance = client.choose("lab-2");
        return instance.getUri().toString();
    }
    @RequestMapping(value = "/instancesl")
    public String getLessonInstancesRun(){
        ServiceInstance instance = client.choose("lessonService");
        return instance.getUri().toString();
    }

    /* TEACHERS */
    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getTeacher(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getLessonInstancesRun();
        log.info("Getting all details for teacher " + id + " from " + url);
        var response = restTemplate.exchange(String.format("%s/teachers/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Teacher.class, id);

        log.info("Info about teacher: " + id);

        //sendGroupMessage(response, id, HttpMethod.GET, response.getBody().toString());
        return response.getBody().toString();
    }
    @RequestMapping(value = "/teachers", method = RequestMethod.GET, produces="application/json")
    public String getTeachers() {
        String url = getLessonInstancesRun();
        log.info("Getting all teacher" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/teachers", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All teachers: \n" + response;
    }
    @RequestMapping(value = "/teachers", method = RequestMethod.POST, produces="application/json")
    public String createTeacher(@RequestBody String object) {
        String url = getLessonInstancesRun();
        log.info("Posting Teacher from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/teachers", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        //sendStudentMessage(response, Long.valueOf(0), HttpMethod.POST, response.getBody().toString());
        return "All Teachers: \n" + response.getBody();
    }
    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateTeacher(@RequestBody String object, @PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Updating Room from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/teachers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        //sendStudentMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated Teacher: \n" + response.getBody();
    }
    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.DELETE, produces="application/json")
    public String deleteTeacher(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Deleting Teacher from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/teachers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted Teacher with ID: " + id :
                "Some error when delete Teacher with ID:" + id;
       /* sendStudentMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);*/
        return "{\"result\":\"" + result + "\"}";
    }

    /* BUILDINGS */
    @RequestMapping(value = "/buildings/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getBuilding(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getLessonInstancesRun();
        log.info("Getting all details for building " + id + " from " + url);
        var response = restTemplate.exchange(String.format("%s/buildings/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Building.class, id);

        log.info("Info about building: " + id);

        sendGroupMessage(response, id, HttpMethod.GET, response.getBody().toString());
        return response.getBody().toString();
    }
    @RequestMapping(value = "/buildings", method = RequestMethod.GET, produces="application/json")
    public String getBuildings() {
        String url = getLessonInstancesRun();
        log.info("Getting all buildings" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/buildings", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All buildings: \n" + response;
    }
    @RequestMapping(value = "/buildings", method = RequestMethod.POST, produces="application/json")
    public String createBuilding(@RequestBody String object) {
        String url = getLessonInstancesRun();
        log.info("Posting building from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        var response = this.restTemplate.exchange(String.format("%s/buildings", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendGroupMessage(response, new Long(0), HttpMethod.POST, response.getBody());
        return "Posted building: \n" + response.getBody();
    }
    @RequestMapping(value = "/buildings/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateBuilding(@RequestBody String object, @PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Updating building from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/buildings/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        sendGroupMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated building: \n" + response.getBody();
    }
    @RequestMapping(value = "/buildings/{id}", method = RequestMethod.DELETE/*, produces="application/json"*/)
    public String deleteBuilding(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Deleting building from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/buildings/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted building with ID: " + id :
                "Some error when delete building with ID:" + id;
        sendGroupMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return result;//"{\"result\":\"" + result + "\"}";
    }

    /* ROOMS */
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET, produces="application/json")
    public String getRoom(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Getting all details for Room " + id + " from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms/%s", url, id),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, id);
        sendStudentMessage(response, id, HttpMethod.GET, response.getBody().toString());
        log.info("Info about Room: " + response.getBody());

        return "Id -  " + id + " \n Room Details " + response.getBody();
    }
    @RequestMapping(value = "/rooms", method = RequestMethod.GET, produces="application/json")
    public String getRooms() {
        String url = getLessonInstancesRun();
        log.info("Getting all Rooms from " + url);
        String response = this.restTemplate.exchange(String.format("%s/rooms", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All Rooms: \n" + response;
    }
    @RequestMapping(value = "/rooms", method = RequestMethod.POST, produces="application/json")
    public String createRoom(@RequestBody String object) {
        String url = getLessonInstancesRun();
        log.info("Posting Room from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendStudentMessage(response, Long.valueOf(0), HttpMethod.POST, response.getBody().toString());
        return "All Rooms: \n" + response.getBody();
    }
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateRoom(@RequestBody String object, @PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Updating Room from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        sendStudentMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated Room: \n" + response.getBody();
    }
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.DELETE, produces="application/json")
    public String deleteRoom(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Deleting Room from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted room with ID: " + id :
                "Some error when delete room with ID:" + id;
        sendStudentMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return "{\"result\":\"" + result + "\"}";
    }

    /* SUBJECTS */
    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getSubject(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getLessonInstancesRun();
        log.info("Getting all details for subject " + id + " from " + url);
        var response = restTemplate.exchange(String.format("%s/subjects/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Subject.class, id);

        log.info("Info about subject: " + id);

        //sendGroupMessage(response, id, HttpMethod.GET, response.getBody().toString());
        return response.getBody().toString();
    }
    @RequestMapping(value = "/subjects", method = RequestMethod.GET, produces="application/json")
    public String getSubjects() {
        String url = getLessonInstancesRun();
        log.info("Getting all subject" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/subjects", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All subjects: \n" + response;
    }
    @RequestMapping(value = "/subjects", method = RequestMethod.POST, produces="application/json")
    public String createSubject(@RequestBody String object) {
        String url = getLessonInstancesRun();
        log.info("Posting Subject from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/subjects", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        //sendStudentMessage(response, Long.valueOf(0), HttpMethod.POST, response.getBody().toString());
        return "All Subjects: \n" + response.getBody();
    }
    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateSubject(@RequestBody String object, @PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Updating Room from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/subjects/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        //sendStudentMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated Subject: \n" + response.getBody();
    }
    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.DELETE, produces="application/json")
    public String deleteSubject(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        log.info("Deleting Subject from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/subjects/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted Subject with ID: " + id :
                "Some error when delete Subject with ID:" + id;
       /* sendStudentMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);*/
        return "{\"result\":\"" + result + "\"}";
    }


    /* GROUPS */
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.GET, produces = "application/json")
    public String getGroup(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getInstancesRun();
        log.info("Getting all details for group " + id + " from " + url);
        var response = restTemplate.exchange(String.format("%s/groups/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Group.class, id);

        log.info("Info about group: " + id);

        sendGroupMessage(response, id, HttpMethod.GET, response.getBody().toString());
        return response.getBody().toString();
    }
    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces="application/json")
    public String getGroups() {
        String url = getInstancesRun();
        log.info("Getting all groups" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/groups", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All groups: \n" + response;
    }
    @RequestMapping(value = "/groups", method = RequestMethod.POST, produces="application/json")
    public String createGroup(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting group from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        var response = this.restTemplate.exchange(String.format("%s/groups", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendGroupMessage(response, new Long(0), HttpMethod.POST, response.getBody());
        return "Posted group: \n" + response.getBody();
    }
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateGroup(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating group from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/groups/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        sendGroupMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated group: \n" + response.getBody();
    }
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.DELETE/*, produces="application/json"*/)
    public String deleteGroup(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting group from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/groups/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted group with ID: " + id :
                "Some error when delete group with ID:" + id;
        sendGroupMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return result;//"{\"result\":\"" + result + "\"}";
    }
    @RequestMapping(value = "/groups/{id}/students", method = RequestMethod.GET, produces = "application/json")
    public String getGroupStudents(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getInstancesRun();
        log.info("Getting all students for group " + id + " from " + url);

        String response = this.restTemplate.exchange(String.format("%s/groups/%s/students", url, Long.toString(id)),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "Students of group with id " + id + "ID:" + response;
    }
    @RequestMapping(value = "/groups/{id}/students/{studentId}", method = RequestMethod.DELETE/*, produces="application/json"*/)
    public String removeStudentFromGroup(@PathVariable Long id, @PathVariable Long studentId) {
        String url = getInstancesRun();
        log.info("Deleting student " + studentId + "# from group " + id + "# from: " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/groups/%s/students/%s", url, id, studentId),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully removed student " + studentId + "# from group " + id + "#" :
                "Some error when remove student " + studentId + "# from group " + id + "#" ;
        sendGroupMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return result;//"{\"result\":\"" + result + "\"}";
    }

    /* STUDENTS */
    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET, produces="application/json")
    public String getStudent(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Getting all details for Student " + id + " from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, id);
        sendStudentMessage(response, id, HttpMethod.GET, response.getBody().toString());
        log.info("Info about Student: " + response.getBody());

        return "Id -  " + id + " \n Student Details " + response.getBody();
    }
    @RequestMapping(value = "/students", method = RequestMethod.GET, produces="application/json")
    public String getStudents() {
        String url = getInstancesRun();
        log.info("Getting all Students from " + url);
        String response = this.restTemplate.exchange(String.format("%s/students", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All Students: \n" + response;
    }
    @RequestMapping(value = "/students", method = RequestMethod.POST, produces="application/json")
    public String createStudent(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting Student from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendStudentMessage(response, Long.valueOf(0), HttpMethod.POST, response.getBody().toString());
        return "All Students: \n" + response.getBody();
    }
    @RequestMapping(value = "/students/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateStudent(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating Student from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        sendStudentMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated Student: \n" + response.getBody();
    }
    @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE, produces="application/json")
    public String deleteStudent(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting Student from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted student with ID: " + id :
                "Some error when delete student with ID:" + id;
        sendStudentMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return "{\"result\":\"" + result + "\"}";
    }
    @RequestMapping(value = "/students/{id}/group", method = RequestMethod.GET, produces="application/json")
    public String getGroupOfStudent(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Getting group of student " + id + "# from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s/group", url, id),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, id);
        sendStudentMessage(response, id, HttpMethod.GET, response.getBody().toString());
        log.info("Info about group: " + response.getBody());

        return "Group of student " + id + " ID\n" + response.getBody();
    }
    @RequestMapping(value = "/students/{id}/group/{groupId}", method = RequestMethod.POST, produces="application/json")
    public String addStudentToGroup(@PathVariable Long id, @PathVariable Long groupId) {
        String url = getInstancesRun();
        log.info("Add student " + id + "# to group " + groupId + "# from " + url);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s/group/%s", url, id, groupId),
                HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                });
//        sendStudentMessage(response, Long.valueOf(0), HttpMethod.POST, response.getBody().toString());
        return "All Students: \n" + response.getBody();
    }

    @RequestMapping(value="/info-producer",method=RequestMethod.GET, produces="application/json")
    public String info(){
        ObjectNode root = producer.info();

        return root.toString();
    }

    /* MESSAGES */
    @RequestMapping(value = "/messages", method = RequestMethod.GET, produces="application/json")
    public String getMessages() {
        String url = getInstancesRun();
        log.info("Getting all messages" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/messages", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All messages: \n" + response;
    }

    private void sendGroupMessage(ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendGroupMsg(
                new GroupMessage(
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        new Timestamp(System.currentTimeMillis()),
                        error
                )
        );
    }
    private void sendStudentMessage(ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendStudentMsg(
                new StudentMessage(
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        new Timestamp(System.currentTimeMillis()),
                        error
                )
        );
    }
}
