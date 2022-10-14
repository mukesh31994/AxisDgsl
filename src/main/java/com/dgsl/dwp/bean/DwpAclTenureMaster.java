package com.dgsl.dwp.bean;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="dwp_acl_tenure_master")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class DwpAclTenureMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@Column(name = "tenure_name")
	@NotEmpty(message = "Please provide a tenure name")
	private String tenureName;
	
	@NotEmpty(message = "Please provide a tenure value")
	private String tenureValue;
	
	@NotEmpty(message = "Please provide a ratecode value")
	private String rateCode;
	
	@Column (name ="is_active",columnDefinition = "boolean default true")
	private boolean active;
}
