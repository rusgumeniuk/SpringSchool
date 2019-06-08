package com.example;
import com.example.messages.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import org.springframework.security.core.Authentication;
        import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

//import javax.ws.rs.core.GenericEntity;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
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

    /* REFRESH */
    @PostMapping(value = "/refreshing", produces = "application/json; charset=UTF-8")
    public String checkRefresh() throws JsonProcessingException {
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

    @Autowired
    UserService userRep;

    @Autowired
    RoleService roleRep;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username or password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");
        return "login";
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
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
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

    @GetMapping("/subjects")
    public ModelAndView getSubjects(){
        String url = getLessonInstancesRun();
        log.info("Getting all subjects from " + url);
        List<Subject> subjects = restTemplate.getForObject(String.format("%s/subjects", url), List.class);
        ModelAndView model = new ModelAndView("subjectAll");
        model.addObject("SubjectList", subjects);
        return model;
    }
    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getSubject(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        ModelAndView view = null;
        Subject object = restTemplate.getForObject(String.format("%s/subjects/%s", url, id), Subject.class);
        if(object.getId() == 0){
            view = new ModelAndView("redirect:/subjects");
            view.addObject("result", "We have not Subject with id: #" + id );
        }
        else{
            view = new ModelAndView("subjectDetail");
            view.addObject("Subject", object);
        }
        return view;
    }
    @RequestMapping(value = "/subjects", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createSubject(@ModelAttribute Subject object) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("subjects");
        }
        String url = getLessonInstancesRun();
        log.info("Posting subjects from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Subject> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/subjects", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        ModelAndView view = new ModelAndView("redirect:/subjects");
        return view;
    }
    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateSubject(@ModelAttribute Subject object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("subjects");
        }
        String url = getLessonInstancesRun();
        log.info("Updating subjects from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Subject> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/subjects/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Subject group = restTemplate.getForObject(String.format("%s/subjects/%s", url, id), Subject.class);

        return getSubject(id);
    }
    @RequestMapping(value = "/subjects/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteSubject(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("subjects");
        }
        String url = getLessonInstancesRun();
        log.info("Deleting subjects from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/subjects/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted subject with ID: " + id :
                "Some error when delete subject with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/subjects");
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/rooms")
    public ModelAndView getRoomsView(){
        String url = getLessonInstancesRun();
        log.info("Getting all Rooms from " + url);
        List<Room> rooms = restTemplate.getForObject(String.format("%s/rooms", url), List.class);
        List<Building> buildings = restTemplate.getForObject(String.format("%s/buildings", url), List.class);
        Building nullBuild = new Building();
        nullBuild.setId(-2);
        buildings.add(0, nullBuild);
        ModelAndView model = new ModelAndView("roomAll");
        model.addObject("RoomList", rooms);
        model.addObject("Buildings", buildings);
        return model;
    }
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getRoom(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        ModelAndView view = new ModelAndView("roomDetail");
        Room object = restTemplate.getForObject(String.format("%s/rooms/%s", url, id), Room.class);
        List<Building> buildings = restTemplate.getForObject(String.format("%s/buildings", url), List.class);
        Building nullBuild = new Building();
        nullBuild.setId(-2);
        buildings.add(0, nullBuild);
        if(object.getId() == 0)
            view.addObject("error", "We have not room with id: #" + id );
        else
            view.addObject("Room", object);
        view.addObject("Buildings", buildings);
        return view;
    }
    @RequestMapping(value = "/rooms", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createRoom(@RequestParam("number")Integer number, @RequestParam("building")String building) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("rooms");
        }
        String url = getLessonInstancesRun();
        log.info("Posting Room from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Room> entity = new HttpEntity<>(new Room(number), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });

        if(!building.isEmpty() && (Integer.valueOf(building) > 0 || Integer.valueOf(building) == -2))
        {
            Room[] ar = restTemplate.getForObject(String.format("%s/rooms", url), Room[].class);
            var res = this.restTemplate.exchange(String.format("%s/rooms/%s/building/%s", url,ar[ar.length-1].getId(), building),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        ModelAndView view = new ModelAndView("redirect:/rooms");
        return view;
    }
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateRoom(@RequestParam("number")Integer number, @RequestParam("building")String building, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("rooms");
        }
        String url = getLessonInstancesRun();
        log.info("Updating Room from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Room> entity = new HttpEntity<>(new Room(number), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Room room = restTemplate.getForObject(String.format("%s/rooms/%s", url, id), Room.class);

        if(!building.isEmpty() && (Integer.valueOf(building) > 0 || Integer.valueOf(building) == -2))
        {var res = this.restTemplate.exchange(String.format("%s/rooms/%s/building/%s", url, id, building),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        return getRoom(id);
    }
    @RequestMapping(value = "/rooms/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteRoom(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("rooms");
        }
        String url = getLessonInstancesRun();
        log.info("Deleting Room from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/rooms/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted room with ID: " + id :
                "Some error when delete room with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/rooms");
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/buildings")
    public ModelAndView getBuildingsView(){
        String url = getLessonInstancesRun();
        String lessonUrl = getLessonInstancesRun();
        log.info("Getting all Buildings from " + url);
        List<Building> buildings = restTemplate.getForObject(String.format("%s/buildings", url), List.class);
        List<Teacher> mentors = restTemplate.getForObject(String.format("%s/teachers", lessonUrl), List.class);
        ModelAndView model = new ModelAndView("buildingAll");
        model.addObject("BuildingList", buildings);
        model.addObject("Mentors", mentors);
        return model;
    }
    @RequestMapping(value = "/buildings/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getBuilding(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        ModelAndView view = new ModelAndView("buildingDetail");
        Building object = restTemplate.getForObject(String.format("%s/buildings/%s", url, id), Building.class);
        if(object.getId() == 0)
            view.addObject("error", "We have not building with id: #" + id );
        else
            view.addObject("Building", object);
        return view;
    }
    @RequestMapping(value = "/buildings", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createBuilding(@ModelAttribute Building object) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("buildings");
        }
        String url = getLessonInstancesRun();
        log.info("Posting Building from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Building> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/buildings", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        ModelAndView view = new ModelAndView("redirect:/buildings");
        return view;
    }
    @RequestMapping(value = "/buildings/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateBuilding(@ModelAttribute Building object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("buildings");
        }
        String url = getLessonInstancesRun();
        log.info("Updating Building from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Building> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/buildings/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Building building = restTemplate.getForObject(String.format("%s/buildings/%s", url, id), Building.class);

        return getBuilding(id);
    }
    @RequestMapping(value = "/buildings/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteBuilding(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("buildings");
        }
        String url = getLessonInstancesRun();
        log.info("Deleting Building from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/buildings/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted building with ID: " + id :
                "Some error when delete building with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/buildings");
        view.addObject("result", result);
        return view;
    }

    /* Admin's features */
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
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        String url = getInstancesRun();
        ModelAndView view = new ModelAndView("messageDetail");
        Message message = restTemplate.getForObject(String.format("%s/messages/%s", url, id), Message.class);
        if(message.getMsg_id() == 0)
            view.addObject("error", "We have not message with id: #" + id );
        else
            view.addObject("Message", message);
        return view;
    }

    @GetMapping("/admins")
    public ModelAndView getAdminPage(){
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        return new ModelAndView("createAdmin");
    }
    @PostMapping("/admins")
    public ModelAndView createAdmin(@ModelAttribute Users user, String role){
        user.setEnabled(true);
        userRep.save(user);
        Authorities a = new Authorities(user.getUsername(),"ROLE_"+role.toUpperCase());
        roleRep.save(a);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/allProperties")
    public ModelAndView getPropertiesFromAllModules(){
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        ModelAndView view = new ModelAndView("propertiesAll");
        try {
            String schoolServiceProps = restTemplate.exchange(String.format("%s/properties", getInstancesRun()),
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }).getBody();
            view.addObject("school", schoolServiceProps);
        }
        catch (Exception ex){
            log.info("Error on SCHOOL SERVICE");
            log.info(ex.getMessage());
        }
        try{
            String lessonServiceProps = restTemplate.exchange(String.format("%s/properties", getLessonInstancesRun()),
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }).getBody();
            view.addObject("lesson", lessonServiceProps);
        }
        catch (Exception ex){
            log.info("Error on LESSON SERVICE");
            log.info(ex.getMessage());
        }
        try{
            String serverProps = restTemplate.exchange("http://Ruthless:7777/properties",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }).getBody();
            view.addObject("server", serverProps);
        }
        catch (Exception ex){
            log.info("Error on SERVER");
            log.info(ex.getMessage());
        }
        try {
            view.addObject("client", getPropertiesClient());
        }
        catch (JsonProcessingException e) {
            log.info("Error on CLIENT");
            log.info(e.getMessage());
        }
        return view;
    }
    @PostMapping("/updateAllProperties")
    public ModelAndView updateAllProperties(){

        ResponseEntity response = restTemplate.exchange("http://Ruthless:7777/actuator/bus-refresh",
                HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                });

        ModelAndView view = new ModelAndView("redirect:/allProperties");
        if(response.getStatusCode() == HttpStatus.NO_CONTENT)
            view.addObject("result", "Properties are updated!");
        else
            view.addObject("result", "Something wrong");
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
