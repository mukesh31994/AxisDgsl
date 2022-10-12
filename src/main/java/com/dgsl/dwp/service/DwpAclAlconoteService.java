package com.dgsl.dwp.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.dgsl.dwp.bean.DwpAclAlconoteTemplate;
import com.dgsl.dwp.repository.DwpAlconoteRepository;
import com.lowagie.text.DocumentException;

@Service
public class DwpAclAlconoteService {

	@Autowired
	DwpAlconoteRepository dwpAlconoteRepository;

	public List<DwpAclAlconoteTemplate> getAll() {
		return dwpAlconoteRepository.findAll();
	}

	public ByteArrayInputStream exportAlconotePDF(String templateName, String tableStr, JSONArray transactionData) {
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			String htmlContent = StreamUtils.copyToString(
					new ClassPathResource("templates/" + templateName + ".html").getInputStream(),
					StandardCharsets.UTF_8);

			System.out.println(htmlContent);

			htmlContent = htmlContent.replace("{DATA}", getDataStringForHtml(transactionData));

			htmlContent = htmlContent.replaceAll(",@", "<br />");
			htmlContent = htmlContent.replaceAll("<br>", "<br />");

			htmlContent = htmlContent.replace("{TABLE-DATA}", getTableStringForHtml(tableStr));

			System.out.println(htmlContent);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();
			renderer.createPDF(byteArrayOutputStream, false);
			renderer.finishPDF();
			byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		} catch (DocumentException | IOException | JSONException e) {
			e.printStackTrace();
		}

		return byteArrayInputStream;
	}

	private CharSequence getTableStringForHtml(String tableStr) {
		String tableTemplate = "<table> <thead><tr><th>Status</th><th>Range</th><th>Tenure</th><th>Current Value</th><th>New Value</th><th>RateCode / Currency</th><th>Send By</th><th>Sent To</th></tr> </thead><tbody>";
		try {
			JSONArray lJSONArray = new JSONArray(tableStr);
			for (int i = 0; i < lJSONArray.length(); i++) {
				JSONObject lRow = lJSONArray.getJSONObject(i);
				System.out.println(lRow);
				tableTemplate += "<tr><td>"+lRow.get("status")+"</td><td>" +lRow.getString("range").replace("<", "&lt;").replace(">", "&gt;")+"</td><td>"+lRow.get("tenure")+"</td><td>"+lRow.get("currentValue")+"</td><td>"+lRow.get("newValue")+"</td><td>"
						+lRow.get("rateCode")+" : " + lRow.get("currency") + "</td><td>"+lRow.getString("sentBy")+"</td><td>"+lRow.getString("sentTo")+"</td></tr>";
			}
			tableTemplate += "</tbody></table>";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tableTemplate;
	}

	public String getDataStringForHtml(JSONArray transactionData) throws JSONException {

		String fnlTemplateString = "";
		for (int i = 0; i < transactionData.length(); i++) {
			String templateString = "<div><div class=\"dataClass\"><div class=\"headPoint\"><p>{HEAD}</p></div><div><p>{BODY}</p></div></div></div>";

			JSONObject lObject = (JSONObject) transactionData.get(i);
			templateString = templateString.replace("{HEAD}", lObject.getString("header"));
			templateString = templateString.replace("{BODY}", lObject.getString("body"));
			fnlTemplateString += templateString;
		}

		return fnlTemplateString;
	}

}
