package de.hska.iwi.vislab.lab5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableOAuth2Client
@Controller
public class FibonacciWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(FibonacciWebApplication.class, args);
	}

	@Value("${oauth.resource:http://localhost:8080}")
	private String baseUrl;

	@Value("${oauth.token:http://localhost:8080/oauth/token}")
	private String tokenUrl;

	@Autowired
	private OAuth2RestOperations restTemplate;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("counter", new Counter());
		getCountersList(model);
		return "fibapp";
	}

	@GetMapping("/fib")
	public String getCountersList(Model model) {
		model.addAttribute("counter", new Counter());
		String list = restTemplate.getForObject(baseUrl + "/", String.class);
		model.addAttribute("counterlist", list);
		return "fibapp";
	}

	@RequestMapping(path = "/addnew")
	public String createCounter(Model model) {
		model.addAttribute("counter", new Counter());
		restTemplate.postForObject(baseUrl + "/", null, String.class);
		getCountersList(model);
		return "fibapp";
	}

	@PostMapping(path = "/nextfib")
	public String increaseCounter(@ModelAttribute("counter") Counter counter, Model model) {
		model.addAttribute("counter", counter);
		restTemplate.postForObject(baseUrl + "/" + counter.getId(), null, String.class);
		getCountersList(model);
		return "fibapp";
	}

	@PostMapping("/delfib")
	public String deleteCounter(@ModelAttribute("counter") Counter counter, Model model) {
		model.addAttribute("counter", counter);
		restTemplate.delete(baseUrl + "/" + counter.getId());
		getCountersList(model);
		return "fibapp";
	}

	@Bean
	public OAuth2RestOperations restTemplate() {
		return new OAuth2RestTemplate(resource());
	}

	@Bean
	protected OAuth2ProtectedResourceDetails resource() {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientSecret("secret");
		resource.setClientId("my-client-with-secret");
		return resource;
	}

}
