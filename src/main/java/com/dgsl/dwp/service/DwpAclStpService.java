package com.dgsl.dwp.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DwpAclStpService {
	
	@Autowired
	EntityManagerFactory emf;
	
//	@PersistenceContext
//	private EntityManager em;
	
	public void dirStp() {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		     
		List<Object[]> results = em.createQuery("SELECT rm.sentBy,rm.sentTo,r.id,r.rangeName,t.tenureName,rm.tenureId, t.id FROM dwp_acl_inr_rates_master rm join dwp_acl_range_master r on rm.rangeId=r.id join dwp_acl_tenure_master t on rm.tenureId = t.id").getResultList();
//		List<Object[]> results = em.createQuery("SELECT r.sentBy as sent, rn.rangeName as range FROM dwp_acl_inr_rates_master r join dwp_acl_range_master rn on r.rangeId=rn.id").getResultList();
		 
		for (Object[] result : results) {
		    System.out.println(result[0] + " " + result[1] );
		}
		 
		em.getTransaction().commit();
		em.close();

	}

}
