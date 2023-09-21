package com.jboussouf.labmanagement.repository;

import com.jboussouf.labmanagement.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleReposetory extends JpaRepository<AppRole, Long> {
    public AppRole findByRoleName(String roleName);
}
