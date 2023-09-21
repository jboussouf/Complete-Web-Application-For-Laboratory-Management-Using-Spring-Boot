package com.jboussouf.labmanagement.repository;

import com.jboussouf.labmanagement.model.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {
    public WaitingList findByUsername(String username);
}
