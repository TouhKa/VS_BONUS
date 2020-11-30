package de.hska.iwi.vislab.lab2.fibonacci;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FibonacciResourceTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient();
		// I don't care about null being forbidden. Fight me.
		c.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

		// uncomment the following line if you want to enable
		// support for JSON in the client (you also have to uncomment
		// dependency on jersey-media-json module in pom.xml and
		// Main.startServer())
		// --
		// c.configuration().enable(new
		// org.glassfish.jersey.media.json.JsonJaxbFeature());

		target = c.target(Main.BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.shutdown();
	}

	@Test
	public void testCounting() {
		Response response = target.path("/fib").request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(response.getStatus(), 200);
		// create two counters
		String response2 = target.path("/fib").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
		assertEquals(response2, "0");
		String response3 = target.path("/fib").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
		assertEquals(response3, "1");
		// get the initial value of the second counter (id 1)
		String response4 = target.path("/fib/1").request().accept(MediaType.TEXT_PLAIN).get(String.class);
		assertEquals(response4, "0");
		// increment the counter 13 times
		for (int i = 0; i < 13; i++) {
			String re = target.path("/fib/1").request().accept(MediaType.TEXT_PLAIN).put(null, String.class);
			assertEquals(re, "Success!");
		}
		// query again
		String response5 = target.path("/fib/1").request().accept(MediaType.TEXT_PLAIN).get(String.class);
		// the 14th fibonacci number is 233
		assertEquals(response5, "233");
		// remove the counter
		String response6 = target.path("/fib/1").request().accept(MediaType.TEXT_PLAIN).delete(String.class);
		assertEquals(response6, "Counter removed!");


	}

}
