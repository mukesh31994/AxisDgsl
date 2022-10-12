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

public class ReadPropertyFile {

	@Autowired
	ObjectFactory<HttpSession> httpSessionFactory;

	public static final String LOCATION = "C:\\Users\\Aniket Pednekar\\Documents\\Properties\\";
	
//	private static String LOCATION = "/was/IBM/TradAx_Props/Config/TradAxPOD/";

	final static Logger logger = LoggerFactory.getLogger(ReadPropertyFile.class.getName());

	private static ReadPropertyFile obj = null;

	private ReadPropertyFile() {
	}

	// Now we are providing global point of access.
	public static ReadPropertyFile getInstance() {
		if (obj == null) {
			obj = new ReadPropertyFile();
			System.out.println("Getting Values from Properties file");
		}
		return obj;

	}

	// modified for MIME property file chngs
	public Properties getMIMEProp() {
		Properties prop = new Properties();
		InputStream stream = null;
		try {
			logger.info("MIME Prop Properties Path :::: " + "mimeType.properties");
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			stream = loader.getResourceAsStream("mimeType.properties");
			// stream=this.getClass().getClassLoader().getResourceAsStream("mimeType.properties");
			prop.load(stream);
		} catch (IOException e) {
			logger.error("Exception :::: " + e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public Properties getAlconoteConstantProperty() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			logger.info("Properties Path :::: " + LOCATION + "AlconoteProps.properties");
			input = new FileInputStream(LOCATION + "AlconoteProps.properties");
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
