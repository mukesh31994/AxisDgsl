package com.dgsl.dwp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DirReadPropertyFile {

	@Autowired
	ObjectFactory<HttpSession> httpSessionFactory;

	public static final String LOCATION = "D:\\sts workspace\\AXIS-DIR\\wetransfer_mukesh-project-postman_collection-json_2022-10-07_2001\\AXISProject\\AXISProject\\src\\main\\resources\\";
	
//	private static String LOCATION = "/was/IBM/TradAx_Props/Config/TradAxPOD/";

	final static Logger logger = LoggerFactory.getLogger(DirReadPropertyFile.class.getName());

	private static DirReadPropertyFile obj = null;

	private DirReadPropertyFile() {
	}

	// Now we are providing global point of access.
	public static DirReadPropertyFile getInstance() {
		if (obj == null) {
			obj = new DirReadPropertyFile();
			System.out.println("Getting Values from Properties file");
		}
		return obj;

	}

	public Properties getStpConstantProperty() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
//			logger.info("Properties Path :::: " + LOCATION );
			input = new FileInputStream(LOCATION+ "DirStpProps.properties");
			prop.load(input);
		} catch (IOException e) {
			logger.error("Exception :::: " + e.getMessage(), e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
}
