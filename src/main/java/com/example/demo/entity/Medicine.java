package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Entity
@Service
@Transactional
@Table(name = "medicine")
public class Medicine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String note;
	private Integer count;
	@Column(name = "m_check")
	private Boolean mCheck;
	private Boolean morning;
	private Boolean daytime;
	private Boolean night;
	@ManyToOne
	@JoinColumn(name = "users_id")
	private User user;

	//コンストラクタ
	public Medicine() {
	}

	public Medicine(String name, String note, Integer count, User user) {
		this.name = name;
		this.note = note;
		this.count = count;
		this.user = user;

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Boolean getMCheck() {
		return mCheck;
	}

	public void setMCheck(Boolean mcheck) {
		this.mCheck = mcheck;
	}

	public Boolean getMorning() {
		return morning;
	}

	public void setMorning(Boolean morning) {
		this.morning = morning;
	}

	public Boolean getDaytime() {
		return daytime;
	}

	public void setDaytime(Boolean daytime) {
		this.daytime = daytime;
	}

	public Boolean getNight() {
		return night;
	}

	public void setNight(Boolean night) {
		this.night = night;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
