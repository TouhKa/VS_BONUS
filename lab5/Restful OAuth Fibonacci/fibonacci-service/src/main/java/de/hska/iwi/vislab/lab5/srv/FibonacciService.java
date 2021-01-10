package de.hska.iwi.vislab.lab5.srv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableResourceServer
@RestController
public class FibonacciService {

	public static void main(String[] args) {
		SpringApplication.run(FibonacciService.class, args);
	}

	/**
	 * returns a list of all available counters
	 *
	 * @return a list of all available counters and their values in plain text
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/plain")
	public String getAllCounters() {
		CounterList list = FibonacciDB.getInstance().getAllCounters();
		StringBuilder returnBuilder = new StringBuilder();
		returnBuilder.append("Counters:" + "\n");
		for (Integer counterID : list.counters) {
			if (counterID != null) {
				String line = "Counter " + counterID + " with value " + getCounterValue(counterID) + "\n";
				returnBuilder.append(line);
			}
		}
		return returnBuilder.toString();
	}


	/**
	 * creates a new counter and returns the ID
	 *
	 * @return the success message
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "text/plain")
	public String createCounter() {
		return FibonacciDB.getInstance().createCounter();
	}

	/**
	 * returns the ID of a counter.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain")
	public int getCounterValue(@PathVariable("id") Integer id) {
		return FibonacciDB.getInstance().getCounterValue(id);
	}

	/**
	 * updates the counter
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "text/plain")
	public String calculateNext(@PathVariable("id") Integer id) {
		return FibonacciDB.getInstance().calculateNext(id);
	}

	/**
	 * removes the counter
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/plain")
	public String removeCounter(@PathVariable("id") Integer id) {
		return FibonacciDB.getInstance().removeCounter(id);
	}


	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
					.withClient("my-client-with-secret")
					.authorizedGrantTypes("client_credentials")
					.authorities("ROLE_CLIENT")
					.scopes("read")
					.resourceIds("oauth2-resource")
					.secret("secret");
		}

	}
}
