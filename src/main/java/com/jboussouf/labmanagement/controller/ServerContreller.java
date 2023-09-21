package com.jboussouf.labmanagement.controller;

import com.jboussouf.labmanagement.model.AcceptedUsers;
import com.jboussouf.labmanagement.model.Project;
import com.jboussouf.labmanagement.model.Resources;
import com.jboussouf.labmanagement.model.WaitingList;
import com.jboussouf.labmanagement.service.AccountService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ServerContreller {
    @Autowired

    private AccountService accountService;
    @Autowired

    private PasswordEncoder passwordEncoder;
    @Value("${upload.path}")
    private String uploadPath;



    @GetMapping(path={"/", "/home", "/index"})
    public String index(Model model){
        model.addAttribute("publications", this.accountService.loadLastThreeProjects());

        return "index";
    }
    @GetMapping(path={"/public/signin", "/login"})
    public String signin(){
        return "public/signin";
    }
    @GetMapping(path="/public/signup")
    public String signup(){
        return "public/signup";
    }
    @PostMapping(path="/createAccount")
    public String createAccount(@RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "role") String role){
        System.out.println("username: "+username+" email: "+email+" password: "+password+" role: "+role);
        List<AcceptedUsers> acceptedUsersList =  accountService.loadAcceptedUsers();
        WaitingList user = accountService.laodWaiterByUsername(username);
        if (acceptedUsersList.isEmpty()){
            System.out.println("Admin user");
            accountService.newAcceptedUser(new AcceptedUsers(username, email, this.passwordEncoder.encode(password), new ArrayList<>()));
            accountService.roleToAcceptedUser(username, "ADMIN");
            accountService.roleToAcceptedUser(username, role);
            return "public/signin";
        }else if(user==null){
            System.out.println("Waite list user");
            accountService.newUserToList(new WaitingList(username, email, password, new ArrayList<>()));
            accountService.roleToWaiteList(username, role);
            return "public/signin";
        }
        return "redirect:/public/signup";
    }
    @GetMapping(path="/public/search")
    public String search(@RequestParam(name="search") String search, Model model){
        model.addAttribute("search", search);
        model.addAttribute("postes", this.accountService.loadAllProjectContainKeyword(search));
        //System.out.println(search);
        return "/public/search";
    }



    @GetMapping(path="/private_space")
    public String private_space(){
        return "private_space";
    }

    @GetMapping(path="/errorPage")
    public String errorPage(){
        return "errorPage";
    }

    @GetMapping(path = "/notExist")
    public String notExist(Model model){
        //model.addAttribute("message");
        return "notExist";
    }
    // Administrator part
    @GetMapping(path = "/ADMIN/dashboard")
    public String admin_dashboard(Model model){
        //model.addAttribute("user");
        return "ADMIN/dashboard";
    }

    @GetMapping(path = "/ADMIN/waitingList")
    public String admin_waitingList(Model model){
        model.addAttribute("user", accountService.loadWaiteList());
        return "ADMIN/waitingList";
    }

    @GetMapping(path = "/ADMIN/listUsers")
    public String admin_listUsers(Model model){
        model.addAttribute("user", accountService.loadAcceptedUsers());
        return "ADMIN/listUsers";
    }

    @GetMapping(path = "/ADMIN/listProjects")
    public String admin_Projects(Model model){
        return "ADMIN/listProjects";
    }

    @GetMapping(path = "/ADMIN/resources")
    public String admin_resources(Model model){
        model.addAttribute("resources", this.accountService.loadAllResources());
        return "ADMIN/resources";
    }
    @PostMapping(path="/ADMIN/Availability")
    public String ADMIN_Availability(@RequestParam(name="resourceName") String resourceName){
        this.accountService.changeAvailability(resourceName);
        return "redirect:/ADMIN/resources";
    }
    @PostMapping(path="/ADMIN/deleteResource")
    public String ADMIN_deleteResource(@RequestParam(name="resourceId") Long resourceId){
        this.accountService.deleteResource(resourceId);
        return "redirect:/ADMIN/resources";
    }
    @PostMapping(path="/ADMIN/userAccepted")
    public String userAccepted(Model model, @RequestParam(name = "username") String username){
        System.out.println(username);
        WaitingList user = this.accountService.laodWaiterByUsername(username);
        this.accountService.newAcceptedUser(new AcceptedUsers(user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>()));
        user.getRoles().forEach(r->{
            this.accountService.roleToAcceptedUser(user.getUsername(), r.getRoleName());
        });
        this.accountService.removeById(user.getId());
        System.out.println("user added to the list !!!!!");
        return "redirect:/ADMIN/waitingList";
    }
    @PostMapping(path="/ADMIN/deletePerson")
    public String admin_deletePerson(@RequestParam(name="id") Long id){
        this.accountService.removeById(id);
        return "redirect:/ADMIN/waitingList";
    }

    @GetMapping(path="/ADMIN/personal_view")
    public String admin_personal_view(Model model, @RequestParam(name="username") String username){
        System.out.println(username);
        AcceptedUsers user = this.accountService.loadAcceptedByUsername(username);
        model.addAttribute("user", user);
        return "/ADMIN/personal_view";
    }

    @PostMapping(path="/ADMIN/delete")
    public String admin_delete(@RequestParam(name="id") Long id){
        this.accountService.removeUserById(id);

        return "redirect:/ADMIN/listUsers";
    }

    @GetMapping(path="/ADMIN/newRole")
    public String admin_newRole(@RequestParam(name="username") String username, Model model){
        AcceptedUsers user = this.accountService.loadAcceptedByUsername(username);
        model.addAttribute("user", user);
        return "ADMIN/newRole";
    }

    @PostMapping(path="/ADMIN/role")
    public String admin_role(@RequestParam(name="username") String username, @RequestParam(name="role") String role){
        this.accountService.roleToAcceptedUser(username, role);
        return "redirect:/ADMIN/newRole?username="+username;
    }






    // Student part


    @GetMapping(path = "/STUDENT/dashboard")
    public String student_dashboard(Model model){
        //model.addAttribute("user");
        return "STUDENT/dashboard";
    }

    @GetMapping(path = "/STUDENT/listProjects")
    public String student_listProjects(Model model){
        //model.addAttribute("user");
        return "STUDENT/listProjects";
    }

    @PostMapping(path="/STUDENT/newProject")
    public String newArticle(){
        return "STUDENT/newProject";
    }


    @PostMapping(path="/STUDENT/redPost")
    public String STUDENT_redPost(Model model,
                               @RequestParam(name="content") String content,
                               @RequestParam(name="title") String title,
                               @RequestParam(name="header") String header,
                               @RequestParam(name="image_header") String imageHeader,
                               @RequestParam(name="username") String username){
        //System.out.println(username);

        this.accountService.newProject(new Project(title, imageHeader, header, content, new ArrayList<>()));
        this.accountService.addUserToProject(username, title);
        model.addAttribute("project", this.accountService.loadByTitleProject(title));
        model.addAttribute("users", this.accountService.selectNamesAcceptedUsers());
        return "STUDENT/redPost";
    }

    @GetMapping(path="/STUDENT/addCreators")
    public String STUDENT_addCreators(@RequestParam(name="writer") String writer, @RequestParam(name="title") String title,Model model){

        this.accountService.addUserToProject(writer, title);

        model.addAttribute("project", this.accountService.loadByTitleProject(title));
        model.addAttribute("users", this.accountService.selectNamesAcceptedUsers());
        return "STUDENT/redPost";

    }

    @PostMapping(path="/STUDENT/deleteWriter")
    public String STUDENT_deleteWriter(@RequestParam(name="title") String title, @RequestParam(name="user") String user, @RequestParam(name="writer") String writer){
        this.accountService.deleteUserFromProject(user, title);
        return "redirect:/STUDENT/addCreators?writer="+writer+"&title="+title;
    }

    @GetMapping("/public/projects")
    public String nextView(Model model) {
        List<Project> postes = this.accountService.loadAllProjects();
        model.addAttribute("postes",postes );
        model.addAttribute("numP", postes.toArray().length);
        return "public/projects";
    }

    @GetMapping("/public/project/{title}")
    public String viewProduct(@PathVariable("title") String title, Model model) {
        Project p = this.accountService.loadByTitleProject(title);
        System.out.println(p.getBody());
        model.addAttribute("project", p);

        return "public/project";
    }

    // -------------------------------------- DIRECTOR/dashboard -----------------------------

    @GetMapping("/DIRECTOR/dashboard")
    public String director_dashboard(){
        return "DIRECTOR/dashboard";
    }
    @GetMapping("/DIRECTOR/resources")
    public String DIRECTOR_resources(Model model){
        model.addAttribute("resources", this.accountService.loadAllResources());
        return "DIRECTOR/resources";
    }

    @PostMapping(path="/DIRECTOR/newResource")
    public String DIRECTOR_newResources(){
        return "DIRECTOR/newResource";
    }
    @PostMapping(path="/DIRECTOR/addRessource")
    public String DIRECTOR_AddResource(@RequestParam(name="resourceName") String resourceName, @RequestParam(name="Availability") String Availability){
        if (Availability.equals("Available")){
            this.accountService.newResource(new Resources(resourceName, true));
        } else {
            this.accountService.newResource(new Resources(resourceName, false));
        }
        return "redirect:/DIRECTOR/resources";
    }

    @PostMapping(path="/DIRECTOR/edit")
    public String DIRECTOR_editResource(@RequestParam(name="resourceName") String resourceName, Model model){
        model.addAttribute("resource", this.accountService.loadByResourceName(resourceName));
        return "DIRECTOR/editResource";

    }
    @PostMapping(path="/DIRECTOR/applyEdit")
    public String editResource(
            @RequestParam(name="realName") String realName,
            @RequestParam(name="resourceName") String resourceName,
            @RequestParam(name="Availability") String availability) {

        Resources resource = this.accountService.loadByResourceName(realName);
        resource.setResourceName(resourceName);

        boolean isAvailable = availability.equalsIgnoreCase("Available");
        resource.setDisponible(isAvailable);

        return "redirect:/DIRECTOR/resources";
    }

    @PostMapping(path="/DIRECTOR/Availability")
    public String DIRECTOR_Availability(@RequestParam(name="resourceName") String resourceName){
        this.accountService.changeAvailability(resourceName);
        return "redirect:/DIRECTOR/resources";
    }

    @PostMapping(path="/DIRECTOR/deleteResource")
    public String DIRECTOR_deleteResource(@RequestParam(name="resourceId") Long resourceId){
        this.accountService.deleteResource(resourceId);
        return "redirect:/ADMIN/resources";
    }

    //-------------------------------------- PROFESSOR -------------------------------------------------------------------- //
    @GetMapping(path="/PROF/dashboard")
    public String PROF_dashboard(){
        return "/PROF/dashboard";
    }

    @GetMapping(path="/PROF/projects")
    public String PROF_projects(Model model, @RequestParam(name="username") String username){
        model.addAttribute("postes", this.accountService.loadAllUserProject(username));
        return "/PROF/projects";
    }
    @PostMapping(path="/PROF/newProject")
    public String PROF_newProject(Model model){
        return "PROF/newProject";
    }
    @PostMapping(path="/PROF/redPost")
    public String Prof_redPost(Model model,
                               @RequestParam(name="content") String content,
                               @RequestParam(name="title") String title,
                               @RequestParam(name="header") String header,
                               @RequestParam(name="image_header") String imageHeader,
                               @RequestParam(name="username") String username){
        //System.out.println(username);

        this.accountService.newProject(new Project(title, imageHeader, header, content, new ArrayList<>()));
        this.accountService.addUserToProject(username, title);
        model.addAttribute("project", this.accountService.loadByTitleProject(title));
        model.addAttribute("users", this.accountService.selectNamesAcceptedUsers());
        return "PROF/redPost";
    }

    @GetMapping(path="/PROF/addCreators")
    public String PROF_addCreators(@RequestParam(name="writer") String writer, @RequestParam(name="title") String title,Model model){

        this.accountService.addUserToProject(writer, title);

        model.addAttribute("project", this.accountService.loadByTitleProject(title));
        model.addAttribute("users", this.accountService.selectNamesAcceptedUsers());
        return "PROF/redPost";

    }

    @PostMapping(path="/PROF/deleteWriter")
    public String PROF_deleteWriter(@RequestParam(name="title") String title, @RequestParam(name="user") String user, @RequestParam(name="writer") String writer){
        this.accountService.deleteUserFromProject(user, title);
        return "redirect:/PROF/addCreators?writer="+writer+"&title="+title;
    }

}
