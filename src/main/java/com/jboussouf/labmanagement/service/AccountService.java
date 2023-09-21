package com.jboussouf.labmanagement.service;

import com.jboussouf.labmanagement.model.*;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public WaitingList newUserToList(WaitingList userInfo);
    public AcceptedUsers newAcceptedUser(AcceptedUsers userInfo);
    public AppRole newRole(AppRole role);
    public List<AppRole> loadAllRoles();
    public void roleToWaiteList(String username, String roleName);
    public void roleToAcceptedUser(String username, String roleName);
    public List<WaitingList> loadWaiteList();
    public List<AcceptedUsers> loadAcceptedUsers();
    public WaitingList laodWaiterByUsername(String username);
    public AcceptedUsers loadAcceptedByUsername(String username);
    public AcceptedUsers loadAcceptedUSerByEmail(String email);
    public void removeById(Long id);
    public void removeUserById(Long id);
    public List<String> selectNamesAcceptedUsers();
    // -------------------------------------- Project ------------------------------------------
    public Project newProject(Project project);
    public Project loadByTitleProject(String title);
    public void addUserToProject(String username, String title);
    //public Project loadById(Long id);
    public List<Project> loadAllProjects();
    public List<Project> loadLastThreeProjects();
    public List<Project> loadAllProjectContainKeyword(String keyword);
    public void deleteUserFromProject(String username, String title);
    public List<Project> loadAllUserProject(String username);
    // -------------------------------------- Resources --------------------------------------------
    public Resources loadByResourceName(String resourceName);
    public Resources newResource(Resources resources);
    public List<Resources> loadAllResources();
    public Resources changeAvailability(String resourceName);
    public void deleteResource(Long id);
}
