package com.dgsl.dwp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dgsl.dwp.bean.DwpAclRangeMaster;

public interface DwpAclRangeMasterRepository  extends JpaRepository<DwpAclRangeMaster,Long> {
	List<DwpAclRangeMaster> findByRateCode(String rateCode);
	
	List<DwpAclRangeMaster> findByRateCodeAndActive(String rateCode,boolean active);
	
	List<DwpAclRangeMaster> findByRateCodeAndSpreadAndActive(String rateCode,boolean spreaa, boolean active);
}
