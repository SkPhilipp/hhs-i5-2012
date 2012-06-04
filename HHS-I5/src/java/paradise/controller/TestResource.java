package paradise.controller;

import javax.ejb.Stateless;
import javax.ws.rs.*;

/**
 * Test REST resource.
 * 
 * @author Philipp
 */

@Stateless
@Path("/test")
public class TestResource {

    @GET
    @Produces("text/html")
    public String get() {
        return "Test";
    }

    @PUT
    @Consumes("text/plain")
    public void put(String content) {
        System.out.println(content);
    }

}
