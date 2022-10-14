package com.dgsl.dwp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
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
		     
		List<Tuple> results = em.createNativeQuery("SELECT rm.sent_by,rm.sent_to,r.id,r.range_name,t.tenure_name,rm.tenure_id, t.id as tid FROM dwp_acl_inr_rates_master rm join dwp_acl_range_master r on rm.range_id=r.id join dwp_acl_tenure_master t on rm.tenure_id = t.id",Tuple.class).getResultList();
//		List<Object[]> results = em.createQuery("SELECT rm.sentBy,rm.sentTo,r.id,r.rangeName,t.tenureName,rm.tenureId, t.id FROM dwp_acl_inr_rates_master rm join dwp_acl_range_master r on rm.rangeId=r.id join dwp_acl_tenure_master t on rm.tenureId = t.id").getResultList();
//		List<Object[]> results = em.createQuery("SELECT r.sentBy as sent, rn.rangeName as range FROM dwp_acl_inr_rates_master r join dwp_acl_range_master rn on r.rangeId=rn.id").getResultList();
		List<Map<String, Object>> result = convertTuplesToMap(results);
		
		 
		em.getTransaction().commit();
		em.close();

	}
	
	public static List<Map<String, Object>> convertTuplesToMap(List<Tuple> tuples) {
	    List<Map<String, Object>> result = new ArrayList<>();
	    for (Tuple single : tuples) {
	        Map<String, Object> tempMap = new HashMap<>();
	        for (TupleElement<?> key : single.getElements()) {
	            tempMap.put(key.getAlias(), single.get(key));
	        }
	        result.add(tempMap);
	    }
	    return result;
	}

}
