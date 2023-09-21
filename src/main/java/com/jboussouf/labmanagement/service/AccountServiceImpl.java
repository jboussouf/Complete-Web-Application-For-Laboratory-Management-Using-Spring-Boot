package com.jboussouf.labmanagement.service;

import com.jboussouf.labmanagement.model.*;
import com.jboussouf.labmanagement.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AcceptedUsersRepository acceptedUsersRepository;
    private WaitingListRepository waitingListRepository;
    private AppRoleReposetory appRoleReposetory;
    private ProjectRepository projectRepository;
    private ResourcesRepository resourcesRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AcceptedUsersRepository acceptedUsersRepository, WaitingListRepository waitingListRepository, AppRoleReposetory appRoleReposetory, ProjectRepository projectRepository, ResourcesRepository resourcesRepository, PasswordEncoder passwordEncoder) {
        this.acceptedUsersRepository = acceptedUsersRepository;
        this.waitingListRepository = waitingListRepository;
        this.appRoleReposetory = appRoleReposetory;
        this.projectRepository = projectRepository;
        this.resourcesRepository = resourcesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public WaitingList newUserToList(WaitingList userInfo) {
        String pwd = userInfo.getPassword();
        userInfo.setPassword(passwordEncoder.encode(pwd));
        return this.waitingListRepository.save(userInfo);
    }

    @Override
    public AcceptedUsers newAcceptedUser(AcceptedUsers userInfo) {
        //String pwd = userInfo.getPassword();
        //userInfo.setPassword(passwordEncoder.encode(pwd));
        return acceptedUsersRepository.save(userInfo);
    }

    @Override
    public AppRole newRole(AppRole role) {
        return appRoleReposetory.save(role);
    }

    @Override
    public void roleToWaiteList(String username, String roleName) {
        WaitingList user = this.waitingListRepository.findByUsername(username);
        AppRole role = appRoleReposetory.findByRoleName(roleName);
        user.getRoles().add(role);
    }
    @Override
    public List<AppRole> loadAllRoles(){
        return this.appRoleReposetory.findAll();
    }
    @Override
    public Resources changeAvailability(String resourceName){
        Resources r = this.resourcesRepository.findByResourceName(resourceName);
        r.setDisponible(!r.isDisponible());
        return r;
    }

    @Override
    public void roleToAcceptedUser(String username, String roleName) {
        AcceptedUsers user = this.acceptedUsersRepository.findByUsername(username);
        AppRole role = this.appRoleReposetory.findByRoleName(roleName);
        if (!user.getRoles().contains(role)){
        user.getRoles().add(role);
        }

    }

    @Override
    public List<WaitingList> loadWaiteList() {
        return this.waitingListRepository.findAll();
    }

    @Override
    public List<AcceptedUsers> loadAcceptedUsers() {
        return this.acceptedUsersRepository.findAll();
    }

    @Override
    public WaitingList laodWaiterByUsername(String username) {
        return this.waitingListRepository.findByUsername(username);
    }
    @Override
    public List<String> selectNamesAcceptedUsers(){
        return this.acceptedUsersRepository.findAllUsersUsername();
    }

    @Override
    public AcceptedUsers loadAcceptedByUsername(String username) {
        return this.acceptedUsersRepository.findByUsername(username);
    }

    @Override
    public AcceptedUsers loadAcceptedUSerByEmail(String email) {
        return this.acceptedUsersRepository.findByEmail(email);
    }

    @Override
    public void removeById(Long id) {
        System.out.println("try to delete user from waiting list");
        this.waitingListRepository.deleteById(id);
    }
    @Override
    public void removeUserById(Long id){
        this.acceptedUsersRepository.deleteById(id);
    }

    // ----------------------------- Projects


    @Override
    public Project newProject(Project project) {
        return this.projectRepository.save(project);
    }

    @Override
    public Project loadByTitleProject(String title) {
        return this.projectRepository.findByTitle(title);

    }
    @Override
    public void addUserToProject(String username, String title){
        Project p = this.projectRepository.findByTitle(title);
        AcceptedUsers user = this.acceptedUsersRepository.findByUsername(username);
        if (!p.getWriter().contains(user)){
            p.getWriter().add(user);
        }
    }
    @Override
    public void deleteUserFromProject(String username, String title){
        Project p = this.projectRepository.findByTitle(title);
        AcceptedUsers user = this.acceptedUsersRepository.findByUsername(username);
        if (p.getWriter().contains(user)){
            p.getWriter().remove(user);
        }
    }
    @Override
    public List<Project> loadAllProjectContainKeyword(String keyword){
        return this.projectRepository.findByTitleContaining(keyword);
    }

    public List<Project> loadAllUserProject(String username){
        return this.projectRepository.selectProjectOfUser(username);
    }

    @Override
    public List<Project> loadLastThreeProjects(){
        return this.projectRepository.findLastThree();
    }
    @Override
    public List<Project> loadAllProjects() {
        return this.projectRepository.findAllDiscrete();
    }

    @Override
    public Resources loadByResourceName(String resourceName) {
        return this.resourcesRepository.findByResourceName(resourceName);
    }

    @Override
    public Resources newResource(Resources resources) {
        return this.resourcesRepository.save(resources);
    }
    @Override
    public void deleteResource(Long id){
        this.resourcesRepository.deleteById(id);
    }
    @Override
    public List<Resources> loadAllResources() {
        return this.resourcesRepository.findAll();
    }

}
