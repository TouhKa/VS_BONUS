package de.hska.iwi.vislab.fibonacci.continuous;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
;

public class FibonacciTest {

	static {

		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "999999");

		// dump http on client
		// System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		// dump http on server		
		//System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
	}

	// endpoint address
	private static final String FIBONACCI_URL =
			"http://localhost:4434/fibonacciservice";

	// server endpoint
	private Endpoint ep;

	@BeforeTest
	public void initServer() {
		// start the server
		ep = Endpoint.publish(FIBONACCI_URL, new FibonacciServiceImpl());

	}

	@Test
	public void testGetNextFibonacci() throws Exception {
		// create a client stub for the service just based on the URL
		Service service = Service.create(new URL(FIBONACCI_URL + "?wsdl"), new QName(
				"http://continuous.fibonacci.vislab.iwi.hska.de/",
				"FibonacciServiceImplService"));

		// create a proxy object for the fibonacci service interface
		FibonacciServiceIntf fibonacciService = service
				.getPort(FibonacciServiceIntf.class);


		// call the service 27 times
		// I adjusted the service to run 27 times as the initial 0 and 1 were forgotten.
		int max = 27;
		int result = 1;
		for (int i = 1; i <= max; i++) {
			if (i > 1)
				result = fibonacciService.getNextFibonacci();
			    System.out.println(result);
		}
		// test the 27th f*-nr
		assertEquals(result, 75025);

		// reset the sequence
		fibonacciService.resetSequence();
		// first number has to be 0
		assertEquals(fibonacciService.getNextFibonacci(), 0);
		// check the next numbers
		assertEquals(fibonacciService.getNextFibonacci(), 1);
		assertEquals(fibonacciService.getNextFibonacci(), 1);
		assertEquals(fibonacciService.getNextFibonacci(), 2);
		assertEquals(fibonacciService.getNextFibonacci(), 3);
		assertEquals(fibonacciService.getNextFibonacci(), 5);
		assertEquals(fibonacciService.getNextFibonacci(), 8);
		assertEquals(fibonacciService.getNextFibonacci(), 13);
		assertEquals(fibonacciService.getNextFibonacci(), 21);
		assertEquals(fibonacciService.getNextFibonacci(), 34);
		assertEquals(fibonacciService.getNextFibonacci(), 55);
		assertEquals(fibonacciService.getNextFibonacci(), 89);
		assertEquals(fibonacciService.getNextFibonacci(), 144);
		// reset again
		fibonacciService.resetSequence();
		assertEquals(fibonacciService.getNextFibonacci(), 0);
		assertEquals(fibonacciService.getNextFibonacci(), 1);
		assertEquals(fibonacciService.getNextFibonacci(), 1);
		assertEquals(fibonacciService.getNextFibonacci(), 2);
		assertEquals(fibonacciService.getNextFibonacci(), 3);
		assertEquals(fibonacciService.getNextFibonacci(), 5);
		assertEquals(fibonacciService.getNextFibonacci(), 8);
	}

	@AfterTest
	public void stopServer() {
		// stop the server
		//JOptionPane.showMessageDialog(null, "TestWsServer beenden");
		ep.stop();
	}
}
