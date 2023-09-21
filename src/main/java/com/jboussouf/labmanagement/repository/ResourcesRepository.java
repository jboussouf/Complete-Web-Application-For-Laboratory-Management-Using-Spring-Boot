package com.jboussouf.labmanagement.repository;

import com.jboussouf.labmanagement.model.Project;
import com.jboussouf.labmanagement.model.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {
    public Resources findByResourceName(String resourceName);
}
