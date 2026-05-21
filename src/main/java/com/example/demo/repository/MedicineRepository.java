package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
	List<Medicine> findByUserId(Integer userId);

	List<Medicine> findByMCheck(Boolean mCheck);

}
