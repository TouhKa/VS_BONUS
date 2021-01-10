package de.hska.iwi.vislab.lab2.fibonacci;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "fib" path).
 */
@Path("/fib")
public class FibonacciResource {


    /**
     * returns a list of all available counters
     * @return a list of all available counters in XML form
     */
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public CounterList getAllCounters() {
        return FibonacciDB.getInstance().getAllCounters();
    }
    

    /**
     * creates a new counter and returns the ID
     * 
     * @return the ID of the newly created counter
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public int createCounter() {
        return FibonacciDB.getInstance().createCounter();
    }

    /**
     * returns the ID of a counter.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public int getCounterValue(@PathParam("id") Integer id) {
        return FibonacciDB.getInstance().getCounterValue(id);
    }

    /**
     * updates the counter
     */
    @POST
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String calculateNext(@PathParam("id") Integer id) {
        return FibonacciDB.getInstance().calculateNext(id);
    }

    /**
     * removes the counter
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String removeCounter(@PathParam("id") Integer id) {
        return FibonacciDB.getInstance().removeCounter(id);
    }
}
