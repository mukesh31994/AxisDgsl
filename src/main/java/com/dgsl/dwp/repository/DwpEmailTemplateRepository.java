package com.dgsl.dwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dgsl.dwp.bean.DwpAclEmailTemplate;

@Repository
public interface DwpEmailTemplateRepository extends JpaRepository<DwpAclEmailTemplate, Long> {

	@Query(value = "select id,fromStage,action,toStage,fromMail,toMail,cc,subject,body from DwpAclEmailTemplate where toStage =:toStage and fromStage =:fromStage and action =:action")
	public DwpAclEmailTemplate getEmailTemplate(@Param("toStage") String toStage, @Param("fromStage") String fromStage,
			@Param("action") String action);
	
//	@Query(value = "select id,fromStage,action,toStage,fromMail,toMail,cc,subject,body from DwpAclEmailTemplate where toStage =:toStage and fromStage =:fromStage and action =:action")
	public DwpAclEmailTemplate findByToStageAndFromStageAndAction(@Param("toStage") String toStage, @Param("fromStage") String fromStage,
			@Param("action") String action);
	
	

}
