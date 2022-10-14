package com.dgsl.dwp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dgsl.dwp.bean.DwpAclTenureMaster;



@Repository
public interface DwpAclTenureMasterRepository extends JpaRepository <DwpAclTenureMaster,Long>{

	List<DwpAclTenureMaster> findByRateCode(String rateCode);
	
}
