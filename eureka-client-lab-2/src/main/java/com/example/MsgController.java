package com.example;

import com.example.messages.GroupMessage;
import com.example.messages.StudentMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
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

    @PostMapping(value = "/refreshing", produces = "application/json; charset=UTF-8")
    public String checkRefresh() throws JsonProcessingException
    {
        return refresh() + getPropertiesClient();
    }

    @PostMapping(value = "/actuator/bus-refresh", produces = "application/json; charset=UTF-8")
    public String refresh()
    {
        return "Refreshed";
    }

    @GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
    public String getPropertiesClient() throws JsonProcessingException
    {
        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
        for (String propertyName : bootstrapProperties.getPropertyNames()) {
            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(props);
    }

    @RequestMapping(value = "/instances")
    public String getInstancesRun(){
        ServiceInstance instance = client.choose("lab-2");
        return instance.getUri().toString();
    }

    @RequestMapping(value = "/groups/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
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

    @RequestMapping(value="/info-producer",method=RequestMethod.GET, produces="application/json")
    public String info()
    {
        ObjectNode root = producer.info();

        return root.toString();
    }

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
