package com.dgsl.dwp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dgsl.dwp.bean.DwpAclAlconoteTransactionData;
import com.dgsl.dwp.repository.DwpAlconoteTransactionDataRepository;

@Service
public class DwpAclAlconoteTransactionDataService {

	@Autowired
	DwpAlconoteTransactionDataRepository dwpAlconoteTransactionDataRepository;
	
	public Optional<DwpAclAlconoteTransactionData> findById(String pTransactionId){
		return dwpAlconoteTransactionDataRepository.findById(pTransactionId);
	}
	
	public DwpAclAlconoteTransactionData save(DwpAclAlconoteTransactionData pDwpAclAlconoteTransactionData){
		return dwpAlconoteTransactionDataRepository.save(pDwpAclAlconoteTransactionData);
	}
	
}
