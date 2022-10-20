package com.dgsl.dwp.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.text.StringSubstitutor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dgsl.dwp.bean.DwpAclEmailTemplate;
import com.dgsl.dwp.repository.DwpEmailTemplateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DwpAclEmailerService {

	@Autowired
	DwpEmailTemplateRepository dwpEmailTemplateRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	public DwpAclEmailTemplate getEmailTemplate(String toStep, String fromStep, String action) {
		return dwpEmailTemplateRepository.findByToStageAndFromStageAndAction(toStep, fromStep, action);
	}

	public String sendMail(DwpAclEmailTemplate pDwpAclEmailTemplate) throws Exception {
		System.out.println("In sendMail");

		try {

			InternetAddress[] parsed;
			try {
				parsed = InternetAddress.parse(pDwpAclEmailTemplate.getToMail());
			} catch (AddressException e) {
				throw new IllegalArgumentException("Not valid email: " + pDwpAclEmailTemplate.getToMail(), e);
			}

			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			mailMessage.setSubject(pDwpAclEmailTemplate.getSubject(), "UTF-8");

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");

			if (pDwpAclEmailTemplate.getCc() != null && !pDwpAclEmailTemplate.getCc().equals(""))
				helper.setCc(InternetAddress.parse(pDwpAclEmailTemplate.getCc()));
			helper.setFrom(pDwpAclEmailTemplate.getFromMail());
			helper.setTo(parsed);
			helper.setText(createMailBody(pDwpAclEmailTemplate.getBody()), true);

			javaMailSender.send(mailMessage);

		} catch (MailException | IllegalArgumentException | JSONException | JsonProcessingException
				| MessagingException e) {
			throw e;
		}
		return "Mail Sent Successfully...";

	}

	public String createMailBody(String mailBodyTemplate)
			throws IllegalArgumentException, JSONException, JsonMappingException, JsonProcessingException {

//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, Object> valuesMap = mapper.convertValue(Constant.getBodyEnity(),
//				new TypeReference<Map<String, Object>>() {
//				});

		HashMap<String, Object> valuesMap = (HashMap<String, Object>) new ObjectMapper()
				.readValue(getBodyEnity().toString(), new TypeReference<Map<String, Object>>() {
				});

		System.out.println("Map : " + valuesMap);
		StringSubstitutor sub = new StringSubstitutor(valuesMap);

		String bodyStr = sub.replace(mailBodyTemplate);

		System.out.println("Body : " + bodyStr);
		return bodyStr;
	}

	public static JSONObject getBodyEnity() throws JSONException {
		String JSONStr = "{\"requestNo\":\"DGSL123456\", \"depositRate\":123518, \"empID\":145258, \"department\":\"IMP\", \"link\":\"www.google.com\"}";
		JSONObject lJsonObject = new JSONObject(JSONStr);
		System.out.println(lJsonObject);
		return lJsonObject;
	}
}
