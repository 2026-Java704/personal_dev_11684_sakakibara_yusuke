package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping("/medicine/{id}/edit")
	public String edit(
			@PathVariable Integer id,
			Model model) {
		Medicine medicine = medicineRepository.findById(id).get();
		model.addAttribute("medicine", medicine);
		return "editMedicine";
	}

	@PostMapping("/medicine/{id}/edit")
	public String update(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String note,
			@RequestParam(defaultValue = "") Integer count,
			@RequestParam(defaultValue = "") Boolean mcheck) {
		Medicine medicine = medicineRepository.findById(id).get();
		medicine.setName(name);
		medicine.setNote(note);
		medicine.setCount(count);
		medicine.setMcheck(mcheck);

		medicineRepository.save(medicine);
		return "redirect:/medicine";
	}
}
