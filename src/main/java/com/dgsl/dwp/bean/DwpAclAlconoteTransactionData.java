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
@AllArgsConstructor
@Data
@Getter
@Setter
@RequiredArgsConstructor
public class DwpAclAlconoteTransactionData {

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private String transactionId;

	private String data;

}
