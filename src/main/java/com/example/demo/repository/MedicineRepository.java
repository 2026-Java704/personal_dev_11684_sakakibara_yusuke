package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
	List<Medicine> findByUserId(Integer userId);

	List<Medicine> findByMCheck(Boolean mCheck);

	List<Medicine> findByUserIdAndMorningTrue(Integer userId);

	List<Medicine> findByUserIdAndDaytimeTrue(Integer userId);

	List<Medicine> findByUserIdAndNightTrue(Integer userId);

	List<Medicine> findByMorningTrue();

	List<Medicine> findByDaytimeTrue(); // 昼用を追加

	List<Medicine> findByNightTrue(); // 晩用を追加

}
