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
        add(new Employee(1, "1234", "1@o.p"));
        add(new Employee(2, "2334", "2@o.p"));
        add(new Employee(3, "3434", "3@o.p"));
        add(new Employee(4, "4534", "4@o.p"));
        add(new Employee(5, "5634", "5@o.p"));
    }};

    public List<Employee> findEntities() {
        return objects;
    }

    public Employee findEntity(int id) {
        for(Employee e : objects){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
    }

}
