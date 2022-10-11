package com.dgsl.dwp.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.dgsl.dwp.util.ReadPropertyFile;
import com.dgsl.dwp.util.ServerConnection;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.util.Id;

@Service
public class FileService {

	public String uplodeFile(String pFileLocation, String pTransactionId) {
		String lStatus = "Failed";

		try {
			ServerConnection lServerConnection = new ServerConnection();
			ObjectStore objStore = lServerConnection.getObjectStore();
			Document doc1 = Factory.Document.getInstance(objStore, ClassNames.DOCUMENT, new Id(pTransactionId));
			FileInputStream f = (FileInputStream) doc1.accessContentStream(0);
			// Get the display name of the object store.
			String lObjStoreName = objStore.get_DisplayName();
			System.out.println("Object store name = " + lObjStoreName);

			// Create a document instance.
			Document doc = Factory.Document.createInstance(objStore, ClassNames.DOCUMENT);

			File files = new File(pFileLocation);

			String lFileName = files.getName();

			FileInputStream fileInput = new FileInputStream(files);

			String mimeType = "application/docs";
			String extension = lFileName.substring(lFileName.lastIndexOf(".") + 1);

			if (extension != null && extension != "") {
				if (ReadPropertyFile.getInstance().getMIMEProp().getProperty(extension.toUpperCase()) != null) {
					mimeType = ReadPropertyFile.getInstance().getMIMEProp().getProperty(extension.toUpperCase());
				}
			}

			doc.getProperties().putValue("DocumentTitle", lFileName);
			doc.set_MimeType(mimeType); // if its your pdf then set mimetype for PDF

			if (fileInput != null) {
				ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
				contentTransfer.setCaptureSource(fileInput);
				ContentElementList contentElementList = Factory.ContentElement.createList();
				contentElementList.add(contentTransfer);
				doc.set_ContentElements(contentElementList);
				contentTransfer.set_RetrievalName(lFileName);
				doc.set_MimeType(mimeType);
			}

			// Check in the document.
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

			doc.save(RefreshMode.NO_REFRESH);

			/*
			 * // Create Sub Folder String parentFolderPath =
			 * ReadPropertyFile.getInstance().getPropConst().getProperty("documentClassPath"
			 * ); Folder parentFolder = Factory.Folder.getInstance(objStore, null,
			 * parentFolderPath); Folder newFolder =
			 * parentFolder.createSubFolder(pTransactionId);
			 * newFolder.save(RefreshMode.NO_REFRESH);
			 */
			
			// Create Sub Folder
			String parentFolderPath = ReadPropertyFile.getInstance().getPropConst().getProperty("documentClassPath");
			com.filenet.api.core.Folder parentFolder = Factory.Folder.fetchInstance(objStore, parentFolderPath, null);

			boolean newFoldercreate = true;

			FolderSet subFolders = parentFolder.get_SubFolders();
			Iterator it = subFolders.iterator();
			while (it.hasNext()) {
				com.filenet.api.core.Folder subFolder = (com.filenet.api.core.Folder) it.next();
				String name = ((com.filenet.api.core.Folder) subFolder).get_FolderName();
				System.out.println("Subfolder = " + name);
				if (pTransactionId.equals(name)) {
					newFoldercreate = false;
				}

			}

			if (newFoldercreate) {
				Folder newFolder = parentFolder.createSubFolder(pTransactionId);
				newFolder.save(RefreshMode.NO_REFRESH);
			}

			// File the document.
			Folder folder = Factory.Folder.fetchInstance(objStore, parentFolderPath + "\\" + pTransactionId, null);

			ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, lFileName,
					DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
			rcr.save(RefreshMode.REFRESH);
			lStatus = "Success";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lStatus;
	}

	public Document getDocument(String pDocumentId) {
		Document lDocument = null;
		try {
			ServerConnection lServerConnection = new ServerConnection();

			ObjectStore lObjectStore = lServerConnection.getObjectStore();
			lDocument = Factory.Document.fetchInstance(lObjectStore, new Id(pDocumentId), null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lDocument;

	}

}