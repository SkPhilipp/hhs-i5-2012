package paradise.controller;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import paradise.model.TestObject;

/**
 * Test REST resource.
 * 
 * @author Philipp
 */

@Stateless
@Path("/test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource implements Serializable {

    @GET
    public TestObject get() {
        return new TestObject();
    }

    @PUT
    public void put() {
        System.out.println("Line");
    }

}
