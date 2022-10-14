package com.dgsl.dwp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dgsl.dwp.bean.DwpAclInrRatesMaster;


public interface DwpAclInrRatesMasterRepository extends JpaRepository<DwpAclInrRatesMaster, Long> {

	DwpAclInrRatesMaster findById(long id);
	
	List<DwpAclInrRatesMaster> findByRateCode(String rateCode);
	
	List<DwpAclInrRatesMaster> findByInternalStatus(String internalStatus);
	
	List<DwpAclInrRatesMaster> findByStatus(String status);
	
}
