package paradise.service;

import java.sql.Date;
import java.util.HashMap;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import paradise.model.Employee;

/**
 * @author Groep3
 */
@Stateless
@Path("/employee") 
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    HashMap<Integer, Employee> employeeMap = new HashMap<Integer, Employee>(){{
        this.put(1, new Employee(1, "status", "sex", new Date(0), "fax", "ss-nr", "email", 0, 0, "CV URL"));
        this.put(2, new Employee(2, "status", "sex", new Date(0), "fax", "ss-nr", "email", 0, 0, "CV URL"));
    }};

    @GET
    @Path("{id}")    
    public Employee get(@PathParam("id") Integer id) {
        if(employeeMap.containsKey(id)){
            return employeeMap.get(id);
        }
        else{
            return null;
        }
    }

    @PUT
    @Path("{id}")   
    public Response put(@PathParam("id") String id) {
        return Response.ok().build();
    }

}
