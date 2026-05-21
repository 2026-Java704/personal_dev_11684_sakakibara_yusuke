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
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.UserRepository;

@Controller

public class MedicineController {

	private final UserRepository userRepository;
	private final MedicineRepository medicineRepository;
	private final HttpSession session;
	private final Account account;

	public MedicineController(
			MedicineRepository medicineRepository,
			HttpSession session,
			Account account, UserRepository userRepository) {
		this.medicineRepository = medicineRepository;
		this.session = session;
		this.account = account;
		this.userRepository = userRepository;
	}

	@GetMapping("/medicine")
	public String index(Model model) {
		List<Medicine> medicineList = medicineRepository.findByUserId(account.getId());
		model.addAttribute("medicine", medicineList);

		return "medicine";
	}

	@PostMapping("/medicine")
	public String keep(
			@RequestParam(required = true) Boolean mcheck,
			Model model) {

		List<Medicine> medicine = medicineRepository.findByMCheck(mcheck);
		if (medicine == true) {

		}

		model.addAttribute("mcheck", mcheck);
		return "redirect:/medicine";
	}

	//更新
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
			@RequestParam(defaultValue = "") Integer count) {
		Medicine medicine = medicineRepository.findById(id).get();
		medicine.setName(name);
		medicine.setNote(note);
		medicine.setCount(count);

		medicineRepository.save(medicine);
		return "redirect:/medicine";
	}

	//削除
	@PostMapping("/medicine/{id}/delete")
	public String delete(@PathVariable Integer id) {
		medicineRepository.deleteById(id);
		return "redirect:/medicine";
	}

	//新規
	@GetMapping("/medicine/add")
	public String create() {
		return "addMedicine";
	}

	@PostMapping("/medicine/add")
	public String store(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String note,
			@RequestParam(defaultValue = "0") Integer count) {

		User user = userRepository.findById(account.getId()).get();
		account.setId(user.getuserId());

		Medicine medicine = new Medicine(name, note, count, user);
		medicine.setMCheck(false);
		medicineRepository.save(medicine);
		return "redirect:/medicine";
	}

}
