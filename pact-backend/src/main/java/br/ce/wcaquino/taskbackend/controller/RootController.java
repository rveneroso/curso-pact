package br.ce.wcaquino.taskbackend.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping(value ="/")
public class RootController {

	@GetMapping
	public String hello() {
		return "Hello World!";
	}

	@PostMapping(value = "/barrigaPactStateChange")
	public void stateChange(@RequestBody Map<String, Object> body) {
		String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NTY5NzF9.7x72Q8YcNk7arsO8kpNv7oVtuC2_mmy02ItpnqwhSxg";
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", TOKEN);
		HttpEntity<String> entity = new HttpEntity<String> (headers);
		rest.exchange("https://barrigarest.wcaquino.me/reset", HttpMethod.GET, entity, Object.class);
	}
}
