package com.jboussouf.labmanagement.repository;

import com.jboussouf.labmanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    public Project findByTitle(String title);
    @Query(value = "SELECT p FROM Project p ORDER BY p.id DESC LIMIT 3")
    public List<Project> findLastThree();
    public List<Project> findByTitleContaining(String keyword);
    @Query(value = "SELECT p FROM Project p ORDER BY p.id DESC")
    public List<Project> findAllDiscrete();
    @Query("SELECT p FROM Project p INNER JOIN p.writer u WHERE u.username = :username ORDER BY p.id DESC")
    public List<Project> selectProjectOfUser(@Param("username") String username);

}
