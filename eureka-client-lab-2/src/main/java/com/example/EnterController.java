package com.example;
import com.example.lessons.Lesson;
import com.example.lessons.LessonNumber;
import com.example.lessons.LessonType;
import com.example.lessons.WeekMode;
import com.example.messages.Message;
import com.example.security.Authority;
import com.example.security.RoleRepository;
import com.example.security.User;
import com.example.security.UserRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

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
    UserRepository userRep;

    @Autowired
    RoleRepository roleRep;

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username or password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");
        return "login";
    }
    @GetMapping("/registration")
    public String registration(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        return "registration";
    }
    @PostMapping("/registration")
    public String registration(@ModelAttribute User user, String role) {
        user.setEnabled(true);
        userRep.save(user);
        Authority a = new Authority(user.getUsername(),"ROLE_"+role.toUpperCase());
        roleRep.save(a);
        return "redirect:/login";
    }

    @GetMapping("/students")
    public ModelAndView getStudentsView(){
        ModelAndView model = new ModelAndView("studentAll");
        try{
            String url = getInstancesRun();
            log.info("Getting all Students from " + url);
            List<Student> students = restTemplate.getForObject(String.format("%s/students", url), List.class);
            List<Group> groups = restTemplate.getForObject(String.format("%s/groups", url), List.class);
            groups.add(0, null);
            model.addObject("StudentList", students);
            model.addObject("Groups", groups);
        }
        catch (Exception ex){
            sendMessage("Student", "Error when Getting all Students", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
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
        ModelAndView model = new ModelAndView("groupAll");
        try{
            String url = getInstancesRun();
            String lessonUrl = getLessonInstancesRun();
            log.info("Getting all Groups from " + url);
            List<Group> groups = restTemplate.getForObject(String.format("%s/groups", url), List.class);
            List<Teacher> mentors = restTemplate.getForObject(String.format("%s/teachers", lessonUrl), List.class);
            model.addObject("GroupList", groups);
            model.addObject("Mentors", mentors);
        }
        catch (Exception ex){
            sendMessage("Groups", "Error when Getting all groups", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
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
        ModelAndView model = new ModelAndView("subjectAll");
        try{
            String url = getLessonInstancesRun();
            log.info("Getting all subjects from " + url);
            List<Subject> subjects = restTemplate.getForObject(String.format("%s/subjects", url), List.class);
            model.addObject("SubjectList", subjects);
            model.addObject("ControlTypes", ControlType.values());
        }
        catch (Exception ex){
            sendMessage("Subject", "Error when Getting all Subjects", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
    }
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
            view.addObject("ControlTypes", ControlType.values());
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
        ModelAndView model = new ModelAndView("roomAll");
        try{
            String url = getLessonInstancesRun();
            log.info("Getting all Rooms from " + url);
            List<Room> rooms = restTemplate.getForObject(String.format("%s/rooms", url), List.class);
            List<Building> buildings = restTemplate.getForObject(String.format("%s/buildings", url), List.class);
            Building nullBuild = new Building();
            nullBuild.setId(-2);
            buildings.add(0, nullBuild);
            model.addObject("RoomList", rooms);
            model.addObject("Buildings", buildings);
        }
        catch (Exception ex){
            sendMessage("Room", "Error when Getting all Rooms", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
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
        ModelAndView model = new ModelAndView("buildingAll");
        try{
            String url = getLessonInstancesRun();
            String lessonUrl = getLessonInstancesRun();
            log.info("Getting all Buildings from " + url);
            List<Building> buildings = restTemplate.getForObject(String.format("%s/buildings", url), List.class);
            List<Teacher> mentors = restTemplate.getForObject(String.format("%s/teachers", lessonUrl), List.class);
            model.addObject("BuildingList", buildings);
            model.addObject("Mentors", mentors);
        }
        catch (Exception ex){
            sendMessage("Building", "Error when Getting all Building", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
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

    @GetMapping("/teachers")
    public ModelAndView getTeachersView(){
        ModelAndView model = new ModelAndView("teacherAll");
        try{
            String url = getLessonInstancesRun();
            log.info("Getting all Teachers from " + url);
            List<Teacher> teachers = restTemplate.getForObject(String.format("%s/teachers", url), List.class);
            String groupUrl = getInstancesRun();
            List<Group> groups = restTemplate.getForObject(String.format("%s/groups", groupUrl), List.class);
            Group nullGroup = new Group();
            nullGroup.setId(-2);
            groups.add(0, nullGroup);
            model.addObject("TeacherList", teachers);
            model.addObject("Groups", groups);
            model.addObject("Ranks", Arrays.asList(TeacherRank.values()));
        }
        catch (Exception ex){
            sendMessage("Teacher", "Error when Getting all Teachers", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
        return model;
    }
    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getTeacher(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        ModelAndView view = new ModelAndView("teacherDetail");
        Teacher object = restTemplate.getForObject(String.format("%s/teachers/%s", url, id), Teacher.class);
        String groupUrl = getInstancesRun();
        List<Group> groups = restTemplate.getForObject(String.format("%s/groups", groupUrl), List.class);
        Group nullGroup = new Group();
        nullGroup.setId(-2);
        groups.add(0, nullGroup);
        if(object.getId() == 0)
            view.addObject("error", "We have not teacher with id: #" + id );
        else
            view.addObject("Teacher", object);
        view.addObject("Groups", groups);
        view.addObject("Ranks", Arrays.asList(TeacherRank.values()));
        return view;
    }
    @RequestMapping(value = "/teachers", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createTeacher(@RequestParam("fullName")String fullName, @RequestParam("cathedra")String cathedra,
                                      @RequestParam("teacherRank")TeacherRank teacherRank, @RequestParam("mentored_group")Integer mentored_group) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("teachers");
        }

        String url = getLessonInstancesRun();
        log.info("Posting Teacher from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Teacher> entity = new HttpEntity<>(new Teacher(fullName, teacherRank, cathedra), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/teachers", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        if(mentored_group > 0){
            Teacher[] ar = restTemplate.getForObject(String.format("%s/teachers", url), Teacher[].class);
            var res = this.restTemplate.exchange(String.format("%s/teachers/%s/group/%s", url,ar[ar.length-1].getId(), mentored_group),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        ModelAndView view = new ModelAndView("redirect:/teachers");
        return view;
    }
    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateTeacher(@RequestParam("fullName")String fullName, @RequestParam("cathedra")String cathedra,
                                      @RequestParam("teacherRank")TeacherRank teacherRank, @RequestParam("mentored_group")Integer mentored_group, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("teachers");
        }
        String url = getLessonInstancesRun();
        log.info("Updating Teacher from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Teacher> entity = new HttpEntity<>(new Teacher(fullName, teacherRank, cathedra), headers);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/teachers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Teacher teacher = restTemplate.getForObject(String.format("%s/teachers/%s", url, id), Teacher.class);

        if(mentored_group > 0 || mentored_group == -2){
            var res = this.restTemplate.exchange(String.format("%s/teachers/%s/group/%s", url, id, mentored_group),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        return getTeacher(id);
    }
    @RequestMapping(value = "/teachers/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteTeacher(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("teachers");
        }
        String url = getLessonInstancesRun();
        log.info("Deleting Teacher from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/teachers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted teacher with ID: " + id :
                "Some error when delete teacher with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/teachers");
        view.addObject("result", result);
        return view;
    }



    @GetMapping("/lessons")
    public ModelAndView getLessons(){
        ModelAndView model = new ModelAndView("lessonAll");
        try{
            String schoolUrl = getInstancesRun();
            String lessonUrl= getLessonInstancesRun();
            log.info("Getting all lessons from " + lessonUrl);
            List<Lesson> lessons = null;
            List<Subject> subjects = null;
            List<Group> groups = null;
            List<Room> rooms = null;
            List<Teacher> teachers = null;
            try{
                lessons = restTemplate.getForObject(String.format("%s/lessons", lessonUrl), List.class);
                subjects = restTemplate.getForObject(String.format("%s/subjects", lessonUrl), List.class);
                rooms = restTemplate.getForObject(String.format("%s/rooms", lessonUrl), List.class);
                teachers = restTemplate.getForObject(String.format("%s/teachers", lessonUrl), List.class);
            }
            catch (Exception ex){
                System.out.println("Lesson service doesn't work");
                System.out.println(ex.getMessage());
            }
            try{
                groups = restTemplate.getForObject(String.format("%s/groups", getInstancesRun()), List.class);
            }
            catch (Exception ex){
                System.out.println("School service doesn't work");
                System.out.println(ex.getMessage());
            }

            model.addObject("LessonList", lessons);
            model.addObject("Groups", groups);
            model.addObject("Subjects", subjects);
            model.addObject("Teachers", teachers);
            model.addObject("Rooms", rooms);
            model.addObject("DaysOfWeek", DayOfWeek.values());
            model.addObject("LessonTypes", LessonType.values());
            model.addObject("LessonNumbers", LessonNumber.values());
            model.addObject("WeekModes", WeekMode.values());
        }
        catch (Exception ex){
            sendMessage("Student", "Error when Getting all Students", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
        return model;
    }
    @RequestMapping(value = "/lessons/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getLesson(@PathVariable Long id) {
        String url = getLessonInstancesRun();
        ModelAndView view = null;
        Lesson object = restTemplate.getForObject(String.format("%s/lessons/%s", url, id), Lesson.class);
        if(object.getId() == 0){
            view = new ModelAndView("redirect:/lessons");
            view.addObject("result", "We have not Lesson with id: #" + id );
        }
        else{
            view = new ModelAndView("lessonDetail");
            view.addObject("Lesson", object);


            Subject[] subjects = null;
            Group[] groups = null;
            Room[] rooms = null;
            Teacher[] teachers = null;
            Group group = null;
            Teacher teacher = null;
            Room room = null;
            Subject subject = null;
            try{
                subjects = restTemplate.getForObject(String.format("%s/subjects", url), Subject[].class);
                rooms = restTemplate.getForObject(String.format("%s/rooms", url), Room[].class);
                teachers = restTemplate.getForObject(String.format("%s/teachers", url), Teacher[].class);
                subject= restTemplate.getForObject(String.format("%s/subjects/%s", url, object.getSubject()), Subject.class);
                teacher = restTemplate.getForObject(String.format("%s/teachers/%s", url, object.getTeacher()), Teacher.class);
                room = restTemplate.getForObject(String.format("%s/rooms/%s", url, object.getRoom()), Room.class);

            }
            catch (Exception ex){
                System.out.println("Lesson service doesn't work");
                System.out.println(ex.getMessage());
            }
            try{
                groups = restTemplate.getForObject(String.format("%s/groups", getInstancesRun()), Group[].class);
                group = restTemplate.getForObject(String.format("%s/groups/%s", getInstancesRun(), object.getGroup()), Group.class);
            }
            catch (Exception ex){
                System.out.println("School service doesn't work");
                System.out.println(ex.getMessage());
            }
            view.addObject("Groups", groups);
            view.addObject("Subjects", subjects);
            view.addObject("Teachers", teachers);
            view.addObject("Rooms", rooms);

            view.addObject("Group", group);
            view.addObject("Subject", subject);
            view.addObject("Teacher", teacher);
            view.addObject("Room", room);

            List<DayOfWeek> days = Arrays.asList(DayOfWeek.values());
            view.addObject("DaysOfWeek", days);
            view.addObject("LessonTypes", LessonType.values());
            view.addObject("LessonNumbers", LessonNumber.values());
            view.addObject("WeekModes", WeekMode.values());
        }
        return view;
    }
    @RequestMapping(value = "/lessons", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createLesson(@RequestParam("lessonNumber")LessonNumber lessonNumber,
                                     @RequestParam("lessonType")LessonType lessonType,
                                     @RequestParam("weekMode")WeekMode weekMode,
                                     @RequestParam("dayOfWeek")DayOfWeek dayOfWeek,
                                     @RequestParam("room")Integer room,
                                     @RequestParam("subject")Integer subject,
                                     @RequestParam("teacher")Integer teacher,
                                     @RequestParam("group")Integer group) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("lessons");
        }
        String url = getLessonInstancesRun();
        log.info("Posting lessons from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Room roomObj = null;
        Subject subjectObj = null;
        Teacher teacherObj = null;
        Group groupObj = null;
        boolean isAllExist = true;
        try{
            subjectObj = restTemplate.getForObject(String.format("%s/subjects/%s", url, subject), Subject.class);
            teacherObj = restTemplate.getForObject(String.format("%s/teachers/%s", url, teacher), Teacher.class);
            roomObj = restTemplate.getForObject(String.format("%s/rooms/%s", url, room), Room.class);
        }
        catch (Exception ex){
            isAllExist = false;
            System.out.println("Lesson service doesn't work");
            System.out.println(ex.getMessage());
        }
        try{
            groupObj = restTemplate.getForObject(String.format("%s/groups/%s", getInstancesRun(), group), Group.class);
        }
        catch (Exception ex){
            isAllExist = false;
            System.out.println("School service doesn't work");
            System.out.println(ex.getMessage());
        }
        if(!isAllExist)
            return new ModelAndView("redirect:/lessons");
        HttpEntity<Lesson> entity = new HttpEntity<>(new Lesson(
                lessonNumber, lessonType, weekMode, dayOfWeek, roomObj, subjectObj, teacherObj, groupObj)
                , headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/lessons", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        ModelAndView view = new ModelAndView("redirect:/lessons");
        return view;
    }
    @RequestMapping(value = "/lessons/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateLesson(@ModelAttribute Lesson object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("lessons");
        }
        String url = getLessonInstancesRun();
        log.info("Updating lessons from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Lesson> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/lessons/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Lesson lesson = restTemplate.getForObject(String.format("%s/lessons/%s", url, id), Lesson.class);

        return getLesson(id);
    }
    @RequestMapping(value = "/lessons/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteLesson(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("lessons");
        }
        String url = getLessonInstancesRun();
        log.info("Deleting lessons from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/lessons/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted lesson with ID: " + id :
                "Some error when delete lesson with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/lessons");
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/schedules")
    public ModelAndView getSchedules(){
        ModelAndView model = new ModelAndView("scheduleAll");
        try{
            String url = getInstancesRun();
            List<Group> groups = restTemplate.getForObject(String.format("%s/groups", url), List.class);
            model.addObject("groupList", groups);
        }
        catch (Exception ex){
            sendMessage("Student", "Error when Getting all Students", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
        return model;
    }
    @RequestMapping(value = "/schedules", method = RequestMethod.POST)
    public ModelAndView getGroupSchedule(@RequestParam("groupId") Long groupId) {
        ModelAndView view = new ModelAndView("scheduleDetail");
        Group group = null;
        try{
            group = restTemplate.getForObject(String.format("%s/groups/%s", getInstancesRun(), groupId), Group.class);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        if(group != null){
            try{
                String lessonUrl = getLessonInstancesRun();
                Lesson[] lessons = restTemplate.getForObject(String.format("%s/lessons", lessonUrl), Lesson[].class);
                List<Lesson> groupLesson = Arrays.asList(lessons)
                        .stream()
                        .filter(lesson -> lesson.getGroup().getId() == groupId)
                        .collect(Collectors.toList());
                view.addObject("GroupLessons", groupLesson);
            }
            catch (Exception ex){

                System.out.println(ex.getMessage());
            }
        }
        view.addObject("DaysOfWeek", DayOfWeek.values());
        view.addObject("LessonTypes", LessonType.values());
        view.addObject("LessonNumbers", LessonNumber.values());
        view.addObject("WeekModes", WeekMode.values());
        view.addObject("group", group);
        return view;
    }

    /* Admin's features */
    @GetMapping("/messages")
    public ModelAndView getMessages(){
        ModelAndView model = new ModelAndView("messageAll");
        try{
            if(!isAdmin()){
                return redirectIfHaveNotAccess("");
            }
            String url = getInstancesRun();
            log.info("Getting all messages from " + url);
            List<Message> messages = restTemplate.getForObject(String.format("%s/messages", url), List.class);
            model.addObject("messageList", messages);
        }
        catch (Exception ex){
            sendMessage("Student", "Error when Getting all Students", 0l, HttpMethod.GET, HttpStatus.OK.toString(), ex.getMessage());
            model.addObject("error", ex.getMessage());
        }
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
        if(message.getId() == 0)
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
    public ModelAndView createAdmin(@ModelAttribute User user, String role){
        user.setEnabled(true);
        userRep.save(user);
        Authority a = new Authority(user.getUsername(),"ROLE_"+role.toUpperCase());
        roleRep.save(a);
        sendMessage("Admin", "Created new " + role, 0l, HttpMethod.POST, HttpStatus.CREATED.toString(), role + " username: " + user.getUsername());
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
        if(response.getStatusCode() == HttpStatus.NO_CONTENT){
            view.addObject("result", "Properties are updated!");
            sendMessage("Admin", "Updated all configs", 0l, HttpMethod.POST, HttpStatus.OK.toString(), "No error");
        }
        else
        {
            view.addObject("result", "Something wrong");
            sendMessage("Admin", "Fail when update configs\r\n" + response, 0l, HttpMethod.POST, HttpStatus.INTERNAL_SERVER_ERROR.toString(), response.getBody().toString());
        }
        return view;
    }

    private ModelAndView redirectIfHaveNotAccess(String viewName){
        return redirectIfHaveNotAccess(new ModelAndView("redirect:/" + viewName));
    }
    private ModelAndView redirectIfHaveNotAccess(ModelAndView redirectDesination){
        if(!isAdmin()){
            ModelAndView view = redirectDesination;
            view.addObject("result", getWrongAccessMessage());
            sendMessage("User", "Access failed", 0l, HttpMethod.GET, HttpStatus.METHOD_NOT_ALLOWED.toString(), " User with username '" + getUsername() + "' try do smth forbidden!");
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


    private void sendMessage(Class clas, ResponseEntity response, Long id, HttpMethod httpMethod, String error){
       sendMessage(clas.getName(), response, id, httpMethod, error);
    }
    private void sendMessage(String classname, ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendMessage(
                new Message(
                        classname,
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        error
                )
        );
    }
    private void sendMessage(String classname, String response, Long id, HttpMethod httpMethod, String status, String error){
        producer.sendMessage(
                new Message(
                        classname,
                        response,
                        httpMethod,
                        status,
                        error
                )
        );
    }
}
