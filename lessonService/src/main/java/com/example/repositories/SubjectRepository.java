package com.example.repositories;

import com.example.Subject;
import com.example.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
