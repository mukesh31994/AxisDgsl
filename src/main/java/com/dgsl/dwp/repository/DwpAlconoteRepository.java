package com.dgsl.dwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dgsl.dwp.bean.DwpAclAlconoteTemplate;

@Repository
public interface DwpAlconoteRepository extends JpaRepository<DwpAclAlconoteTemplate, Long> {

}
