package com.dgsl.dwp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.dgsl.dwp.service.FileService;
import com.dgsl.dwp.util.FileUtility;
import com.dgsl.dwp.util.ReadPropertyFile;
import com.filenet.api.core.Document;

@RestController
@RequestMapping("/dwp")
public class DwpAclFileController {

	@Autowired
	FileService fileService;

	@CrossOrigin
	@PostMapping(value = "/uplodeFile")
	public @ResponseBody String uplodeFile(@RequestBody MultipartFile filename,
			@RequestParam("transactionId") String transactionId) {
		String lDocumentId = "";
		System.out.println("File Name :::: " + filename.getOriginalFilename() + " transactionId :::: " + transactionId);
		FileUtility lFileUtility = new FileUtility();
		try {
			String lLocation = lFileUtility.saveFileToFolder(filename);
			lDocumentId = fileService.uplodeFile(lLocation, transactionId);
			lFileUtility.deleteFolder();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return lDocumentId;
	}

	@CrossOrigin
	@GetMapping(value = "/viewDocument")
	public ResponseEntity<StreamingResponseBody> viewDocument(@RequestParam("documentId") String documentId)
			throws Exception {
		// documentId = "{A30F0B65-2D5C-CB7D-86C8-838EEDF00000}";

		System.out.println("documentId ::: " + documentId);

		Document lDocument = fileService.getDocument(documentId);
		String lFileName = lDocument.get_Name();

		InputStream resource = lDocument.accessContentStream(0);

		String mimeType = "application/docs";
		String extension = lFileName.substring(lFileName.lastIndexOf(".") + 1);

		if (extension != null && extension != "") {
			if (ReadPropertyFile.getInstance().getMIMEProp().getProperty(extension.toUpperCase()) != null) {
				mimeType = ReadPropertyFile.getInstance().getMIMEProp().getProperty(extension.toUpperCase());
			}
		}
		StreamingResponseBody responseBody = outputStream -> {

			int numberOfBytesToWrite;
			byte[] data = new byte[1024];
			while ((numberOfBytesToWrite = resource.read(data, 0, data.length)) != -1) {
				outputStream.write(data, 0, numberOfBytesToWrite);
			}

			resource.close();
		};

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + lFileName)
				.contentType(MediaType.parseMediaType(mimeType)).body(responseBody);
	}

}
