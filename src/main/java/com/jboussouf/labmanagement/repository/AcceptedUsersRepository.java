package com.jboussouf.labmanagement.repository;

import com.jboussouf.labmanagement.model.AcceptedUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcceptedUsersRepository extends JpaRepository<AcceptedUsers, Long> {
    public AcceptedUsers findByUsername(String username);
    public AcceptedUsers findByEmail(String email);
    @Query(value = "SELECT u.username FROM AcceptedUsers u")
    public List<String> findAllUsersUsername();
}
