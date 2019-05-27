package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

        if (response.getStatusCode() == HttpStatus.OK) {
            GroupMessage msg = new GroupMessage("Group was successfully got - " + id.toString(), OperationType.GET, "200", "");
            producer.sendGroupMsg(msg);
        }
        else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            GroupMessage msg = new GroupMessage("Group was unsuccessfully got - " + id.toString(), OperationType.GET, "404", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }
        else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            GroupMessage msg = new GroupMessage("Internal server error when getting group - " + id.toString(), OperationType.GET, "500", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }
        else {
            GroupMessage msg = new GroupMessage("Something gone wrong when getting group - " + id.toString(), OperationType.GET, "", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }

        return response.getBody().toString();
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public String getGroups() {
        String url = getInstancesRun();
        log.info("Getting all groups" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/groups", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All groups: \n" + response;
    }

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    public String createGroup(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting group from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        var response = this.restTemplate.exchange(String.format("%s/groups", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        if (response.getStatusCode() == HttpStatus.CREATED) {
            GroupMessage msg = new GroupMessage("Group was successfully created - ", OperationType.POST, "200", "");
            producer.sendGroupMsg(msg);
        }
        else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            GroupMessage msg = new GroupMessage("Internal server error when creating Group - ", OperationType.POST, "500", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }
        else {
            GroupMessage msg = new GroupMessage("Something gone wrong when creating Group - ", OperationType.POST, "", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }

        return "Posted group: \n" + response.getBody();
    }

    @RequestMapping(value = "/groups/{id}", method = RequestMethod.PUT)
    public String updateGroup(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating group from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        String response = this.restTemplate.exchange(String.format("%s/groups/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id).getBody();

        return "Updated group: \n" + response;
    }

    @RequestMapping(value = "/groups/{id}", method = RequestMethod.DELETE)
    public String deleteGroup(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting group from " + url);
        var response = this.restTemplate.exchange(String.format("%s/groups/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
            GroupMessage msg = new GroupMessage("Group was successfully deleted - " + id.toString(), OperationType.DELETE, "200", "");
            producer.sendGroupMsg(msg);
        }
        else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            GroupMessage msg = new GroupMessage("Group was not found when delete - " + id.toString(), OperationType.DELETE, "404", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }
        else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            GroupMessage msg = new GroupMessage("Internal server error when deleting group - " + id.toString(), OperationType.DELETE, "500", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }
        else {
            GroupMessage msg = new GroupMessage("Something gone wrong when deleting group - " + id.toString(), OperationType.DELETE, "", response.getBody().toString());
            producer.sendGroupMsg(msg);
        }

        return "Deleted group: \n" + id + "\n" + response.getBody();
    }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
    public String getStudent(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Getting all details for Student " + id + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, id).getBody();

        log.info("Info about Student: " + response);

        return "Id -  " + id + " \n Student Details " + response;
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String getStudents() {
        String url = getInstancesRun();
        log.info("Getting all Students from " + url);
        String response = this.restTemplate.exchange(String.format("%s/students", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All Students: \n" + response;
    }

    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public String createStudent(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting Student from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        String response = this.restTemplate.exchange(String.format("%s/students", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All Students: \n" + response;
    }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.PUT)
    public String updateStudent(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating Student from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        String response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id).getBody();

        return "Updated Student: \n" + response;
    }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
    public String deleteStudent(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting Student from " + url);
        var response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id).getBody();

        return "Deleted Student: \n" + response;
    }

    @RequestMapping(value="/info-producer",method=RequestMethod.GET,produces="application/json")
    public String info()
    {
        ObjectNode root = producer.info();

        return root.toString();
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String getMessages() {
        String url = getInstancesRun();
        log.info("Getting all messages" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/messages", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All messages: \n" + response;
    }
}
