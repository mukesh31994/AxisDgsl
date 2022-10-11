package com.dgsl.dwp.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
//@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@RequiredArgsConstructor
public class DwpAclEmailTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

//	@Column(name="from_stage")
	private String fromStage;

//	@Column(name="action")
	private String action;

//	@Column(name="to_stage")
	private String toStage;

//	@Column(name="from_mail")
	private String fromMail;

//	@Column(name="to_mail")
	private String toMail;

//	@Column(name="cc")
	private String cc;

//	@Column(name="subject")
	private String subject;

//	@Column(name="subject")
	private String body;

}
