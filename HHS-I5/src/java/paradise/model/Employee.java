package paradise.model;

import java.sql.Date;

/**
 * @author Philipp
 */
public class Employee {

    public int ID;
    public String status;
    public String sex;
    public Date birthDate;
    public String fax;
    public String socialSecurityNumber;
    public String email;
    public int manager;
    public int branch;
    public String curriculumVitaeURL;

    public Employee(int ID, String status, String sex, Date birthDate, String fax, String socialSecurityNumber, String email, int manager, int branch, String curriculumVitaeURL) {
        this.ID = ID;
        this.status = status;
        this.sex = sex;
        this.birthDate = birthDate;
        this.fax = fax;
        this.socialSecurityNumber = socialSecurityNumber;
        this.email = email;
        this.manager = manager;
        this.branch = branch;
        this.curriculumVitaeURL = curriculumVitaeURL;
    }

}
