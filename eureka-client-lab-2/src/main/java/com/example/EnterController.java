package com.example;

//import org.svenson.JSONParser;
import com.example.messages.Message;
import lombok.experimental.var;
        import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
        import org.springframework.http.*;
import org.springframework.security.core.Authentication;
        import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

//import javax.ws.rs.core.GenericEntity;
        import java.util.*;

@EnableDiscoveryClient
@Controller
public class EnterController {
    @Autowired
    private MsgProducer producer;
    private static Logger log = LoggerFactory.getLogger(EurekaClientLab2Application.class);
    private final RestTemplate restTemplate;

    @Autowired
    public EnterController(RestTemplateBuilder restTemplateBuilder,
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

    @Autowired
    UserService userRep;

    @Autowired
    RoleService roleRep;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username or password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");
        return "menu";
    }
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        return "registration";
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration_post(@ModelAttribute Users users, String role) {
        users.setEnabled(true);
        userRep.save(users);
        Authorities a = new Authorities(users.getUsername(),"ROLE_"+role.toUpperCase());
        roleRep.save(a);
        return "redirect:/login";
    }

    @GetMapping("/students")
    public ModelAndView getStudentsView(){
        String url = getInstancesRun();
        log.info("Getting all Students from " + url);
        List<Student> students = restTemplate.getForObject(String.format("%s/students", url), List.class);
        List<Group> groups = restTemplate.getForObject(String.format("%s/groups", url), List.class);
        groups.add(0, null);
        ModelAndView model = new ModelAndView("studentAll");
        model.addObject("StudentList", students);
        model.addObject("Groups", groups);
        return model;
    }
    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getStudent(@PathVariable Long id) {
        String url = getInstancesRun();
        ModelAndView view = new ModelAndView("studentDetail");
        Student object = restTemplate.getForObject(String.format("%s/students/%s", url, id), Student.class);
        List<Group> groups = restTemplate.getForObject(String.format("%s/groups", url), List.class);
        groups.add(null);
        if(object.getId() == 0)
            view.addObject("error", "We have not student with id: #" + id );
        else
            view.addObject("Student", object);
        view.addObject("Groups", groups);
        return view;
    }
    @RequestMapping(value = "/students", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createStudent(@ModelAttribute Student object) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("students");
        }

        String url = getInstancesRun();
        log.info("Posting Student from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Student> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        //Trouble zone starts here
        if(object.getGroup() != null && !object.getGroup().getTitle().isEmpty() && Integer.valueOf(object.getGroup().getTitle()) > 0){
            Student[] ar = restTemplate.getForObject(String.format("%s/students", url), Student[].class);
            var res = this.restTemplate.exchange(String.format("%s/students/%s/group/%s", url,ar[ar.length-1].getId(), object.getGroup().getTitle()),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        //Trouble zone ends here
        ModelAndView view = new ModelAndView("redirect:/students");
        return view;
    }
    @RequestMapping(value = "/students/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateStudent(@ModelAttribute Student object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("students");
        }
        String url = getInstancesRun();
        log.info("Updating Student from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Student> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Student student = restTemplate.getForObject(String.format("%s/students/%s", url, id), Student.class);

        if(object.getGroup() != null && !object.getGroup().getTitle().isEmpty() && Integer.valueOf(object.getGroup().getTitle()) > 0){
            var res = this.restTemplate.exchange(String.format("%s/students/%s/group/%s", url, id, object.getGroup().getTitle()),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        return getStudent(id);
    }
    @RequestMapping(value = "/students/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteStudent(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("students");
        }
        String url = getInstancesRun();
        log.info("Deleting Student from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/students/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted student with ID: " + id :
                "Some error when delete student with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/students");
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/groups")
    public ModelAndView getGroupsView(){
      String url = getInstancesRun();
      String lessonUrl = getLessonInstancesRun();
      log.info("Getting all Groups from " + url);
      List<Group> groups = restTemplate.getForObject(String.format("%s/groups", url), List.class);
      List<Teacher> mentors = restTemplate.getForObject(String.format("%s/teachers", lessonUrl), List.class);
      ModelAndView model = new ModelAndView("groupAll");
      model.addObject("GroupList", groups);
      model.addObject("Mentors", mentors);
      return model;
  }
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getGroup(@PathVariable Long id) {
        String url = getInstancesRun();
        ModelAndView view = new ModelAndView("groupDetail");
        Group object = restTemplate.getForObject(String.format("%s/groups/%s", url, id), Group.class);
        String lessonUrl = getLessonInstancesRun();
        List<Teacher> mentors = restTemplate.getForObject(String.format("%s/teachers", lessonUrl), List.class);
        if(object.getId() == 0)
            view.addObject("error", "We have not group with id: #" + id );
        else
            view.addObject("Group", object);
        view.addObject("Mentors", mentors);
        return view;
    }
    @RequestMapping(value = "/groups", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createGroup(@ModelAttribute Group object) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("groups");
        }
        String url = getInstancesRun();
        log.info("Posting Group from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Group> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/groups", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        ModelAndView view = new ModelAndView("redirect:/groups");
        return view;
    }
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateGroup(@ModelAttribute Group object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("groups");
        }
        String url = getInstancesRun();
        log.info("Updating Group from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Group> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/groups/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Group group = restTemplate.getForObject(String.format("%s/groups/%s", url, id), Group.class);

        return getGroup(id);
    }
    @RequestMapping(value = "/groups/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteGroup(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("groups");
        }
        String url = getInstancesRun();
        log.info("Deleting Group from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/groups/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted group with ID: " + id :
                "Some error when delete group with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/groups");
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/messages")
    public ModelAndView getMessages(){
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        String url = getInstancesRun();
        log.info("Getting all messages from " + url);
        List<Message> messages = restTemplate.getForObject(String.format("%s/messages", url), List.class);
        ModelAndView model = new ModelAndView("messageAll");
        model.addObject("messageList", messages);
        return model;
    }
    @RequestMapping(value = "/messages/{id}", method = RequestMethod.GET)
    public ModelAndView getMessage(@PathVariable Long id) {
        String url = getInstancesRun();
        ModelAndView view = new ModelAndView("messageDetail");
        Message message = restTemplate.getForObject(String.format("%s/messages/%s", url, id), Message.class);
        if(message.getMsg_id() == 0)
            view.addObject("error", "We have not message with id: #" + id );
        else
            view.addObject("Message", message);
        return view;
    }

    private ModelAndView redirectIfHaveNotAccess(String viewName){
        return redirectIfHaveNotAccess(new ModelAndView("redirect:/" + viewName));
    }
    private ModelAndView redirectIfHaveNotAccess(ModelAndView redirectDesination){
        if(!isAdmin()){
            ModelAndView view = redirectDesination;
            view.addObject("result", getWrongAccessMessage());
            return view;
        }
        return new ModelAndView("/");
    }
    private String getWrongAccessMessage(){
        return "Dear, " + getUsername() + "." +
                "<br>Unfortunately u have not access to do this :c.<br>But u always can upgrade yourself ;-)";
    }
    private boolean isAdmin(){
        return isHasRole("admin");
    }
    private boolean isHasRole(String role){
        if(!role.equalsIgnoreCase("user") &&!role.equalsIgnoreCase("admin"))
            return false;
        var list = getAuthentication().getAuthorities();
        for (var au: list) {
            if(au.getAuthority().equalsIgnoreCase("role_" + role))
                return true;
        }
        return false;
    }
    private String getUsername(){
        return getAuthentication().getName();
    }
    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
