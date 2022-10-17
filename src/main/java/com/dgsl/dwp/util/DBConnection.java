package com.dgsl.dwp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {
	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

	public static Connection getConnectionForFinacle() throws SQLException {
		Connection con = null;
		try {
			if ("true".equalsIgnoreCase(
					DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("dsFlag"))) {

				System.out.println("Before Finacle Datasource testing");
				Context initContext = new InitialContext();
				DataSource ds = (DataSource) initContext
						.lookup(DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("finacleDS"));
				con = ds.getConnection();
				System.out.println("Successful Finacle Datasource testing");

			} else {

				Class.forName(DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("DBDriver"));
				con = DriverManager.getConnection(
						DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("DBConnectURL"),
						DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("DBConnectUserName"),
						DirReadPropertyFile.getInstance().getStpConstantProperty().getProperty("DBConnectPassword"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return con;
	}

}
