package com.dgsl.dwp.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;

import java.util.Date;
import java.util.List;
@Entity(name="dwp_acl_inr_rates_master")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter


public class DwpAclInrRatesMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String withEffectFrom;
	
	private String callable;
	
	@Column(name = "tenure_id")
	private String tenureId;
	
	@Column(name = "range_id")
	private long rangeId;

	private double currentValue;

	private double newValue;
	
	private String transactionId;
	
	private String currency;
	
	private String rateCode;
	
	
	
	
	
	private boolean isChanged;
		
	private String sentBy;
	
	private String sentTo;
	

	private String status;
	
	private String internalStatus;
	
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") 
	 private Date createdDate;
	  
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") 
	  private Date modifiedDate;
	  
	  @Column (name ="is_spread",columnDefinition = "boolean default false")
		private boolean spread;

	
}
