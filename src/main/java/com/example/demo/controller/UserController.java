package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {
	private final HttpSession session;
	private final Account account;
	private final UserRepository userRepository;

	public UserController(
			HttpSession session,
			Account account,
			UserRepository userRepository) {
		this.session = session;
		this.account = account;
		this.userRepository = userRepository;
	}

	@GetMapping({ "/login" })
	public String index() {

		return "login";
	}

	@PostMapping("/login")
	public String login(
			@RequestParam String name,
			@RequestParam String password,
			Model model) {

		if (name.length() == 0 || password.length() == 0) {
			model.addAttribute("message", "ユーザー名とパスワードを入力してください");
			return "login";
		}

		List<User> userList = userRepository.findByNameAndPassword(name, password);
		if (userList == null || userList.size() == 0) {
			model.addAttribute("message", "ユーザー名かパスワードが違います");
			return "login";
		}
		User user = userList.get(0);

		account.setName(user.getName());
		account.setId(user.getuserId());

		return "redirect:/medicine";
	}

	@GetMapping("/account")
	public String create() {
		return "accountForm";
	}

	@PostMapping("/account")
	public String store(
			@RequestParam String name,
			@RequestParam String password,
			Model model) {

		List<String> errorList = new ArrayList<>();
		if (name.length() == 0) {
			errorList.add("名前は必須です");
		}
		if (password.length() == 0) {
			errorList.add("パスワードは必須です");
		}
		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("name", name);
			model.addAttribute("password", password);
			return "accountForm";
		}
		User user = new User(name, password);
		userRepository.save(user);
		return "redirect:/login";
	}

}
