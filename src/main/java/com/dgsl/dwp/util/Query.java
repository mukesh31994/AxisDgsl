package com.dgsl.dwp.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Query {
	private final static Logger logger = LoggerFactory.getLogger(Query.class);

	public void MapToQuery(String pTableName, LinkedHashMap<String, String> pProperty, Connection pConForFinacle)
			throws Exception {
		List<String> columnName = new ArrayList<String>();
		List<String> value = new ArrayList<String>();
		PreparedStatement stmtInsert = null;
		String lQuery = "";
		try {
			for (String key : pProperty.keySet()) {
				columnName.add(key);
				value.add(pProperty.get(key));
			}

			lQuery = "INSERT INTO " + pTableName + " (" + String.join(",", columnName) + ") VALUES ("
					+ String.join(",", value) + ")";
			System.out.println("Query.MapToQuery: Query<" + lQuery + ">");

//			stmtInsert = pConForFinacle.prepareStatement(lQuery);
//			stmtInsert.executeQuery();
		} catch (Exception e) {
			logger.info("Query.MapToQuery: Error occured during process", e.fillInStackTrace());
			throw new Exception(e.getMessage() + " : for Query : " + lQuery);
		}

	}

}
