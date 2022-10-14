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

@Entity(name="dwp_acl_range_master")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class DwpAclRangeMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private long id;
	
	@NotEmpty(message = "Please provide a range name")
	private String rangeName;
	
	@NotEmpty(message = "Please provide a range value")
	private String rangeValue;
	
	@NotEmpty(message = "Please provide a callable")
	private String callable;
	
	private String rateCode;
	
	@Column(name="display_sequence",columnDefinition = "int8 default 0")
	private long displaySequence;
	
	@Column (name ="is_spread",columnDefinition = "boolean default false")
	private boolean spread;
	
	private String spreadRangeList;
	
	@Column (name ="is_active",columnDefinition = "boolean default true")
	private boolean active;
	
}
