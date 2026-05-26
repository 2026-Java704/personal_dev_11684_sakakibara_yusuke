package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String showMedicines(
			@RequestParam(name = "time", defaultValue = "alltime") String time, Model model) {

		if (account.getId() == null) {
			return "login";
		}
		List<Medicine> medicines;
		// パラメータの値によって、取得するデータを切り替える
		if ("daytime".equals(time)) {
			medicines = medicineRepository.findByUserIdAndDaytimeTrue(account.getId());
		} else if ("night".equals(time)) {
			medicines = medicineRepository.findByUserIdAndNightTrue(account.getId());
		} else if ("morning".equals(time)) {
			medicines = medicineRepository.findByUserIdAndMorningTrue(account.getId());
		} else {
			medicines = medicineRepository.findByUserId(account.getId());
			time = "alltime";
		}

		// 画面に「お薬リスト」と「現在選ばれている時間帯」を渡す
		model.addAttribute("medicine", medicines);
		model.addAttribute("currentTime", time);

		return "medicine";
	}

	//	@GetMapping("/medicine")
	//	public String index(
	//			@RequestParam(defaultValue = "") Boolean morning,
	//			Model model) {
	//		List<Medicine> medicineList = medicineRepository.findByUserId(account.getId());
	//		// 1. データベースから朝のフラグがtrueのお薬だけを取得
	//		// 2. 取得したリストを「morningMedicines」という名前でモデルに登録
	//		if (account.getId() == null) {
	//			return "login";
	//		}
	//		model.addAttribute("medicine", medicineList);
	//
	//		List<Medicine> medicineList1 = null;
	//
	//		if (morning == true) {
	//			medicineList1 = medicineRepository.findByMorningTrue();
	//		}
	//		model.addAttribute("medicineList1", medicineList1);
	//
	//		return "medicine";
	//	}

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
			@RequestParam(defaultValue = "false") Boolean mCheck,
			RedirectAttributes redirectAttributes) {

		if (account.getId() == null) {
			return "login";
		}

		Medicine medicine = medicineRepository.findById(id).get();

		// 他人の薬は変更できない
		if (!medicine.getUser().getuserId().equals(account.getId())) {
			return "redirect:/medicine";
		}

		medicine.setMCheck(mCheck);

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd日HH時mm分");
		String currentTime = now.format(formatter);

		redirectAttributes.addFlashAttribute("clickTime", currentTime);
		medicine.setMTime(currentTime);

		medicineRepository.save(medicine);

		return "redirect:/medicine";
	}

	@PostMapping("/medicine/clear-all")
	public String clearAllMedicines() {
		// ログインチェック（未ログインならログイン画面へ）
		if (account.getId() == null) {
			return "login";
		}

		// 1. ログインしているユーザーの薬のリストだけを安全に取得する
		List<Medicine> medicineList = medicineRepository.findByUserId(account.getId());

		// 2. そのユーザーの薬の中から、チェックがついているもの(true)をすべて解除(false)にする
		medicineList.forEach(medicine -> {
			if (Boolean.TRUE.equals(medicine.getMCheck())) {
				medicine.setMCheck(false);
				medicineRepository.save(medicine); // データベースに保存（反映）
			}
		});

		// 3. 更新が終わったら、一覧画面にリダイレクトして再描画
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
			@RequestParam Integer count,
			@RequestParam(defaultValue = "false") Boolean morning,
			@RequestParam(defaultValue = "false") Boolean daytime,
			@RequestParam(defaultValue = "false") Boolean night) {

		Medicine medicine = medicineRepository.findById(id).get();

		// 他人の薬は更新できない
		if (!medicine.getUser().getuserId().equals(account.getId())) {
			return "redirect:/medicine";
		}

		medicine.setName(name);
		medicine.setNote(note);
		medicine.setCount(count);
		medicine.setMorning(morning);
		medicine.setDaytime(daytime);
		medicine.setNight(night);

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
		if (account.getId() == null) {
			return "login";
		}

		return "addMedicine";
	}

	@PostMapping("/medicine/add")
	public String store(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String note,
			@RequestParam(defaultValue = "0") Integer count,
			@RequestParam(defaultValue = "false") Boolean morning,
			@RequestParam(defaultValue = "false") Boolean daytime,
			@RequestParam(defaultValue = "false") Boolean night,
			Model model) {

		if (account.getId() == null) {
			return "login";
		}

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

		Medicine medicine = new Medicine(name, note, count, morning, daytime, night, user);
		model.addAttribute("morning", morning);
		model.addAttribute("daytime", daytime);
		model.addAttribute("night", night);
		medicine.setMCheck(false);
		medicineRepository.save(medicine);
		return "redirect:/medicine";
	}

}
