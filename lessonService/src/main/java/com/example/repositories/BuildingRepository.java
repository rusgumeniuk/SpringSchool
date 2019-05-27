package com.example.repositories;

import com.example.Building;
import com.example.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BuildingRepository extends JpaRepository<Building, Integer> {
}
