package com.dgsl.dwp.util;

import java.io.UnsupportedEncodingException;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class ServerConnection {

//	private final static Logger logger = LoggerFactory.getLogger(ServerConnection.class);
// static ReadPropertyFile bundle = ReadPropertyFile.getInstance();
	UserContext lUser_Context = null;
	boolean isConnection = false;

	private String lObject_Store = "TOS";
	private String lFileNet_URL = "http://10.254.9.59:9080/wsi/FNCEWS40MTOM/";
	private String lUser_Name = "p8admin";
	private String lPassword = "Password123";

	public ObjectStore getObjectStore() throws Exception {
		Domain lDomain = getDomain();
		ObjectStore objStore = Factory.ObjectStore.fetchInstance(lDomain, lObject_Store, null);
		return objStore;
	}

	public Domain getDomain() throws UnsupportedEncodingException {

		Connection lConnection = getConnection();
		Domain domain = Factory.Domain.fetchInstance(lConnection, null, null);
		return domain;

	}

	public Connection getConnection() throws UnsupportedEncodingException {
		Connection lConnection = Factory.Connection.getConnection(lFileNet_URL);
		String lStanzaName = "FileNetP8WSI";
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
