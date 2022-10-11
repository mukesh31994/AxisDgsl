package com.dgsl.dwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dgsl.dwp.bean.DwpAclAlconoteTransactionData;

@Repository
public interface DwpAlconoteTransactionDataRepository extends JpaRepository<DwpAclAlconoteTransactionData, String> {

}
