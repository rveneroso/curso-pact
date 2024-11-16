package br.ce.wcaquino.taskbackend.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value ="/")
public class RootController {

	@GetMapping
	public String hello() {
		return "Hello World!";
	}

	@SuppressWarnings({ "unused", "unchecked", "rawTypes" })
	@PostMapping(value = "/barrigaPactStateChange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> stateChange(@RequestBody Map<String, Object> body) {
		String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NTY5NzF9.7x72Q8YcNk7arsO8kpNv7oVtuC2_mmy02ItpnqwhSxg";
		Map<String, String> response = new HashMap<String, String>();
		String state = (String) body.get("state");

		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", TOKEN);
		HttpEntity<String> entity = new HttpEntity<String> (headers);

		rest.exchange("https://barrigarest.wcaquino.me/reset", HttpMethod.GET, entity, Object.class);
		response.put("reset", "ok");

		switch(state) {
			case "I have an accountId":
				ResponseEntity<List> respAccount = rest.exchange("https://barrigarest.wcaquino.me/contas", HttpMethod.GET, entity, List.class);
				Map<String, Object> firstAccount = (Map<String, Object>) respAccount.getBody().get(0);
				String accountId = firstAccount.get("id").toString();
				response.put("accountId", accountId);
				break;
			default:
				break;
		}

		System.out.println(response);
		return new ResponseEntity<Map<String, String>> (response, HttpStatus.OK);
	}
}
