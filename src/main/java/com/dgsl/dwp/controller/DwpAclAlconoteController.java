package com.dgsl.dwp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.internal.build.AllowSysOut;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dgsl.dwp.bean.DwpAclAlconoteTemplate;
import com.dgsl.dwp.bean.DwpAclAlconoteTransactionData;
import com.dgsl.dwp.service.DwpAclAlconoteService;
import com.dgsl.dwp.service.DwpAclAlconoteTransactionDataService;

import ch.qos.logback.core.net.SyslogOutputStream;

@RestController
@RequestMapping("/dwp")
public class DwpAclAlconoteController {

	@Autowired
	DwpAclAlconoteService dwpAclAlconoteService;

	@Autowired
	DwpAclAlconoteTransactionDataService dwpAclAlconoteTransactionDataService;

	@CrossOrigin
	@RequestMapping("/")
	public ModelAndView index(Map<String, Object> model) {
		return new ModelAndView("index", model);
	}

	@CrossOrigin
	@PostMapping(value = "/getModalData")
	public @ResponseBody String getModalData(@RequestParam(value = "transactionId") String transactionId,
			@RequestParam(value = "transactionData") String transactionData) {
		String lResponse = "";
		try {
			System.out.println(transactionData);
			Optional<DwpAclAlconoteTransactionData> lDwpAclAlconoteTransactionData = dwpAclAlconoteTransactionDataService
					.findById(transactionId);
			if (lDwpAclAlconoteTransactionData.isEmpty()) {
				List<DwpAclAlconoteTemplate> lAlconoteTemplates = dwpAclAlconoteService.getAll();

				DwpAclAlconoteTemplate lAclAlconoteTemplate = new DwpAclAlconoteTemplate();

				lAclAlconoteTemplate.setBody(dwpAclAlconoteService.getTableStringForHtml(transactionData).toString());

				System.out.println(lAlconoteTemplates.size());

				int tableId = lAlconoteTemplates.size() + 1;

				lAclAlconoteTemplate.setDataId("data" + tableId);

				lAclAlconoteTemplate.setHeader("TABLE");

				lAclAlconoteTemplate.setId(tableId);

				lAlconoteTemplates.add(lAclAlconoteTemplate);

				lResponse = dwpAclAlconoteTransactionDataService
						.save(new DwpAclAlconoteTransactionData(transactionId, lAlconoteTemplates.toString()))
						.getData();
			} else {
				lResponse = dwpAclAlconoteTransactionDataService.findById(transactionId).get().getData();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return lResponse;
	}

	@CrossOrigin
	@PostMapping(value = "/saveModalData")
	public @ResponseBody String saveModalData(@RequestParam(value = "data") String data,
			@RequestParam(value = "transactionId") String transactionId) {
		try {
			dwpAclAlconoteTransactionDataService.save(new DwpAclAlconoteTransactionData(transactionId, data));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return "success";
	}

	@RequestMapping("/downloadPdf")
	public void downloadPdf(@RequestParam("tableData") String tableData,
			@RequestParam("transactionId") String transactionId, HttpServletResponse response)
			throws IOException, JSONException {
		String lData = "";
		JSONArray jsonArray = null;
		if (!dwpAclAlconoteTransactionDataService.findById(transactionId).isEmpty())
			jsonArray = new JSONArray(dwpAclAlconoteTransactionDataService.findById(transactionId).get().getData());

		ByteArrayInputStream exportedData = dwpAclAlconoteService.exportAlconotePDF("HTMLforPDF", tableData, jsonArray);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + transactionId + ".pdf");
		IOUtils.copy(exportedData, response.getOutputStream());
	}

}
