package br.com.microservice.statefulanyapi.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservice.statefulanyapi.core.dto.AnyResponse;
import br.com.microservice.statefulanyapi.core.service.AnyService;

@RestController
@RequestMapping("/api/resource")
public class AnyController {

	@Autowired
	private AnyService anyService;
	
	@GetMapping("/")
	public AnyResponse getData(@RequestHeader String accessToken) {
		return anyService.getAnyData(accessToken);
	}
}
