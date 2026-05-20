package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Medicine;
import com.example.demo.model.Account;
import com.example.demo.repository.MedicineRepository;

@Controller

public class MedicineController {
	private final MedicineRepository medicineRepository;
	private final HttpSession session;
	private final Account account;

	public MedicineController(
			MedicineRepository medicineRepository,
			HttpSession session,
			Account account) {
		this.medicineRepository = medicineRepository;
		this.session = session;
		this.account = account;
	}

	@GetMapping("/medicine")
	public String index(Model model) {
		List<Medicine> medicineList = medicineRepository.findByUserId(account.getId());
		model.addAttribute("medicine", medicineList);

		return "medicine";
	}

}
