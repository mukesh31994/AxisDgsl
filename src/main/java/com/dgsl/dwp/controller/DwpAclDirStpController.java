package com.dgsl.dwp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dgsl.dwp.service.DwpAclStpService;

@RestController
@RequestMapping("/dwp")
public class DwpAclDirStpController {

	@Autowired
	DwpAclStpService dwpAclStpService;

	@CrossOrigin
	@GetMapping(value = "/dirStpController")
	public @ResponseBody String dirStpController(@RequestParam(value = "transactionId") String transactionId) {
		return dwpAclStpService.dirStp(transactionId);
	}

}
