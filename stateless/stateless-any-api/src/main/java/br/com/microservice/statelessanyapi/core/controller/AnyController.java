package br.com.microservice.statelessanyapi.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservice.statelessanyapi.core.dto.AnyResponse;
import br.com.microservice.statelessanyapi.core.service.AnyService;

@RestController
@RequestMapping("/api/resource")
public class AnyController {

	@Autowired
	private AnyService anyService;
	
	@GetMapping("/")
	public AnyResponse getResource(@RequestHeader String accessToken) {
		return anyService.getData(accessToken);
	}
}
