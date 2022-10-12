package com.dgsl.dwp.util;

import java.io.UnsupportedEncodingException;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class ServerConnection {

	private final static Logger logger = LoggerFactory.getLogger(ServerConnection.class);
// static ReadPropertyFile bundle = ReadPropertyFile.getInstance();
	UserContext lUser_Context = null;
	boolean isConnection = false;

	private String lObject_Store = ReadPropertyFile.getInstance().getAlconoteConstantProperty()
			.getProperty("ObjectStore");
	private String lFileNet_URL = ReadPropertyFile.getInstance().getAlconoteConstantProperty()
			.getProperty("FileNetURL");
	private String lUser_Name = ReadPropertyFile.getInstance().getAlconoteConstantProperty().getProperty("UserName");
	private String lPassword = ReadPropertyFile.getInstance().getAlconoteConstantProperty().getProperty("Password");

	public ObjectStore getObjectStore() throws Exception {
		logger.info("Object Store :::: " + lObject_Store);
		Domain lDomain = getDomain();
		ObjectStore objStore = Factory.ObjectStore.fetchInstance(lDomain, lObject_Store, null);
		return objStore;
	}

	public Domain getDomain() throws UnsupportedEncodingException {
		logger.info("Getting Domain");
		Connection lConnection = getConnection();
		Domain domain = Factory.Domain.fetchInstance(lConnection, null, null);
		return domain;
	}

	public Connection getConnection() throws UnsupportedEncodingException {
		logger.info("Getting Connection");
		logger.info("Filenet URL :::: " + lFileNet_URL);
		Connection lConnection = Factory.Connection.getConnection(lFileNet_URL);
		String lStanzaName = "FileNetP8WSI";
		logger.info("Stanza Name :::: " + lStanzaName);
		logger.info("User Name :::: " + lUser_Name + " | Password :::: " + lPassword);
		Subject lSubject = UserContext.createSubject(lConnection, lUser_Name, lPassword, lStanzaName);
		UserContext.get().pushSubject(lSubject);
		lUser_Context = UserContext.get();
		setConnection(true);
		return lConnection;
	}

	public void popUserContext() {
		lUser_Context.popSubject();
	}

	public boolean isConnection() {
		return isConnection;
	}

	public void setConnection(boolean isConnection) {
		this.isConnection = isConnection;
	}

}
