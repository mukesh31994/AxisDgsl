package com.dgsl.dwp.service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dgsl.dwp.util.DBConnection;
import com.dgsl.dwp.util.DirReadPropertyFile;
import com.dgsl.dwp.util.Query;

@Service
@Transactional
public class DwpAclStpService {

	Connection conForFinacle = null;

	@Autowired
	EntityManagerFactory emf;

	public String dirStp(String pTransactionId) {
		String lResponse = "";
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			List<Tuple> results = em.createNativeQuery(
					DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("dirStpQuery")
							+ pTransactionId,
					Tuple.class).getResultList();
			List<Map<String, Object>> result = convertTuplesToMap(results);
			lResponse = createStpQuery(result);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			lResponse = e.getMessage();
			e.printStackTrace();
		}
		return lResponse;
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

	public String createStpQuery(List<Map<String, Object>> pMap) {
		String lResponse = "";
		try {
			String[] lTableName = DirReadPropertyFile.getInstance().getStpConstantProperty()
					.getProperty("tableFinacleNames").split(",");
			conForFinacle = DBConnection.getConnectionForFinacle();

			for (String tableName : lTableName) {
				List<String> columnDataType = Arrays.asList(
						DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty(tableName).split(","));
				List<String> hardcodeColumn = Arrays.asList(DirReadPropertyFile.getInstance().getStpConstantProperty()
						.getProperty(tableName + "_HCValue").split(","));
				List<String> functionColumn = Arrays.asList(DirReadPropertyFile.getInstance().getStpConstantProperty()
						.getProperty(tableName + "_Function").split(","));
				for (Map<String, Object> lMap : pMap) {
					LinkedHashMap<String, String> map = new LinkedHashMap<>();
					for (String lData : columnDataType) {
						String[] data = lData.split("#");
						String columnName = data[0];
						String dataType = data[1];
						int length = Integer.parseInt(data[2]);

						if (hardcodeColumn.contains(columnName)) {
							String value = DirReadPropertyFile.getInstance().getStpConstantProperty()
									.getProperty(tableName + "_" + columnName);
							map.put(columnName, convertToValue(value, dataType, length));
						} else if (functionColumn.contains(columnName)) {
							try {
								String value = DirReadPropertyFile.getInstance().getStpConstantProperty()
										.getProperty(tableName + "_" + columnName);
								String ConditionClassName = "com.dgsl.dwp.operation.StpCondition";

								Class<?> ConditionClass = Class.forName(ConditionClassName); // convert string classname
																								// to class
								Object lCondition = ConditionClass.newInstance(); // invoke empty constructor

								Method getNameMethod = lCondition.getClass().getMethod(value, Connection.class);
								String name = null;
								Object lObject = getNameMethod.invoke(lCondition, conForFinacle);
								if (lObject != null)
									name = (String) lObject;
								if (!"Error".equals(name)) {
									map.put(columnName, convertToValue(name, dataType, length));
								} else {
									throw new Exception(value + " : Function not working.");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							String value = DirReadPropertyFile.getInstance().getStpConstantProperty()
									.getProperty(tableName + "_" + columnName);
							map.put(columnName, convertToValue(lMap.get(value) + "", dataType, length));
						}

					}
					Query lQuery = new Query();

					lQuery.MapToQuery(tableName, map, conForFinacle);
				}

			}
			lResponse = "Success";
		} catch (Exception e) {
			lResponse = "Failed, " + e.getMessage();
			e.printStackTrace();
		}
		return lResponse;
	}

	public String convertToValue(String pValue, String pDataType, int pLength) {
		String lResponse = "";

		switch (pDataType.toUpperCase()) {
		case "VARCHAR":
			if (pValue.length() > pLength)
				lResponse = "'" + pValue.substring(0, pLength) + "'";
			else
				lResponse = "'" + pValue + "'";
			break;
		case "NUMBER":
			if (pValue.length() > pLength)
				lResponse = pValue.substring(0, pLength);
			else
				lResponse = pValue;
			break;
		case "DATE":
			SimpleDateFormat lFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = lFormat.parse(pValue);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				lResponse = "TO_DATE('" + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.YEAR) + "','DD-MM-YY')";
				break;
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return lResponse;
	}

}
