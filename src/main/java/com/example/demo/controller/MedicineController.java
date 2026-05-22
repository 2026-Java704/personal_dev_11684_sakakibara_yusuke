package com.example.demo.controller;

import java.util.ArrayList;
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

		if (account.getId() == null) {
			return "login";
		}
		model.addAttribute("medicine", medicineList);

		return "medicine";
	}

	@PostMapping("/medicine")
	public String keep(
			@RequestParam(required = false) Boolean mcheck,
			Model model) {

		model.addAttribute("mcheck", mcheck);
		return "redirect:/medicine";
	}

	@PostMapping("/medicine/{id}/check")
	public String check(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "false") Boolean mCheck) {

		if (account.getId() == null) {
			return "login";
		}

		Medicine medicine = medicineRepository.findById(id).get();

		// 他人の薬は変更できない
		if (!medicine.getUser().getuserId().equals(account.getId())) {
			return "redirect:/medicine";
		}

		medicine.setMCheck(mCheck);

		medicineRepository.save(medicine);

		return "redirect:/medicine";
	}

	//更新
	@GetMapping("/medicine/{id}/edit")
	public String edit(
			@PathVariable Integer id,
			Model model) {

		if (account.getId() == null) {
			return "login";
		}

		Medicine medicine = medicineRepository.findById(id).get();

		// 他人の薬は編集画面を開けない
		if (!medicine.getUser().getuserId().equals(account.getId())) {
			return "redirect:/medicine";
		}

		model.addAttribute("medicine", medicine);
		return "editMedicine";
	}

	@PostMapping("/medicine/{id}/edit")
	public String update(
			@PathVariable Integer id,
			@RequestParam String name,
			@RequestParam String note,
			@RequestParam Integer count) {

		Medicine medicine = medicineRepository.findById(id).get();

		// 他人の薬は更新できない
		if (!medicine.getUser().getuserId().equals(account.getId())) {
			return "redirect:/medicine";
		}

		medicine.setName(name);
		medicine.setNote(note);
		medicine.setCount(count);

		medicineRepository.save(medicine);

		return "redirect:/medicine";
	}

	//削除
	@PostMapping("/medicine/{id}/delete")
	public String delete(@PathVariable Integer id) {

		Medicine medicine = medicineRepository.findById(id).get();

		// 他人の薬は削除できない
		if (!medicine.getUser().getuserId().equals(account.getId())) {
			return "redirect:/medicine";
		}

		medicineRepository.delete(medicine);

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
			@RequestParam(defaultValue = "0") Integer count,
			Model model) {
		List<String> errorMList = new ArrayList<>();
		if (name.length() == 0) {
			errorMList.add("名前は必須です");
		}

		if (errorMList.size() > 0) {
			model.addAttribute("errorMList", errorMList);
			model.addAttribute("name", name);

			return "addMedicine";
		}

		User user = userRepository.findById(account.getId()).get();
		account.setId(user.getuserId());

		Medicine medicine = new Medicine(name, note, count, user);
		medicine.setMCheck(false);
		medicineRepository.save(medicine);
		return "redirect:/medicine";
	}

}
