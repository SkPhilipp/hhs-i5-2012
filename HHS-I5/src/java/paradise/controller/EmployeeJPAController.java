package paradise.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import paradise.model.Employee;

/**
 * @author Groep3
 * STUB
 * TODO: javax.persistence implementation
 */
public class EmployeeJPAController {

    private static List<Employee> objects = new ArrayList<Employee>(){{
        add(new Employee(0, "Status", "M", new Date(0), null, "123-4567-890", "bob@OP", 0, 1, "No CV"));
        add(new Employee(1, "Status", "M", new Date(0), null, "234-5678-901", "bobber@OP", 0, 1, "No CV"));
        add(new Employee(2, "Status", "M", new Date(0), null, "345-6789-012", "bobby@OP", 0, 1, "No CV"));
        add(new Employee(3, "Status", "M", new Date(0), null, "456-7890-123", "bobbytables@OP", 0, 1, "No CV"));
    }};

    public List<Employee> findEntities() {
        return objects;
    }

    public Employee findEntity(int id) {
        for(Employee e : objects){
            if(e.getID() == id){
                return e;
            }
        }
        return null;
    }

}
