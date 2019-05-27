package com.example.repositories;

import com.example.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
