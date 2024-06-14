package com.example.quiz.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quiz")
public class Quiz {

	/**
	 * ���ݩʬOInteger���ɭԡA���[@GeneratedValue ���p�G�ݩʬOint�ɡA�i�[�i���[�A��...
	 * �p�G�S�[�A��ڦbtest��save�ɡA�L���|�^�Ǩ�o��(��sql��ƨ̼˷|�ܧ�) �[�F�A�~�|�^�� �ҥH�q�`���|�[�W@GeneratedValue
	 */

//	�u�nsql�����ϥΨ�AI(Auto Incrememtal�۰ʥͦ��s�X(id���M�i�H�۰ʥͦ��A���ڭ̤]�i�H�ۤv�ۭq�q))�A
//	�N�o�n@GeneratedValue�o�q
//	AI�u��ϥΦb�㦳pk�����W�A�B�@�Ӫ��u�঳�@�ӤĿ�AI�A�B�����@�w�n�Oint(�Ʀr)
//	strategy:��AI���ͦ�����
//	GenerationType.IDENTITY:�N��PK�Ʀr�Ѹ�ƨӦ۰ʼW�[
//	����ɭԭn�Ψ�AI?  ��ڱo"id�L�N�q"���ɭԡA��¥u���y�������ɭ�
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

//	��Ʈw�̭���date�����ϥΫ����U���T��
//	(1)  ���   (2)  �ɶ�    (3)  ����[�ɶ�
//	(1)ocalDate(2)LocalTime(3)LocalDateTime
	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "questions")
	private String questions;

//	���󤣨ϥ�isPublished? �]��get�X�ӫ� �쥻�]�ӬO�nisIsPublished�A���O�o�X�ӫ�o�u��isPublished
//	boolean�ͦ�get��e���|�[is
	@Column(name = "published")
	private boolean published;

	public Quiz() {
		super();
	}

//	�غc��k�i�H"���ݭn"�I��id�]���o�̪�id�O��ۧڭ̪�name�ͦ��y����
	public Quiz(String name, String description, LocalDate startDate, LocalDate endDate, String questions,
			boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questions = questions;
		this.published = published;
	}

//	���F�קKid�i��|�b�ϥΨ�A�ҥH�b�s�W�@�Ө㦳�Ҧ��ݩʪ��غc��k
	public Quiz(int id, String name, String description, LocalDate startDate, LocalDate endDate, String questions,
			boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questions = questions;
		this.published = published;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate starDate) {
		this.startDate = starDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
