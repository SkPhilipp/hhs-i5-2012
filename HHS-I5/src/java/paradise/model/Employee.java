package paradise.model;

import java.sql.Date;

/**
 * @author Groep3
 */
public class Employee {

    private int ID;
    private String status;
    private String sex;
    private Date birthDate;
    private String fax;
    private String socialSecurityNumber;
    private String email;
    private int manager;
    private int branch;
    private String curriculumVitaeURL;

    //TODO: Employee

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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public String getCurriculumVitaeURL() {
        return curriculumVitaeURL;
    }

    public void setCurriculumVitaeURL(String curriculumVitaeURL) {
        this.curriculumVitaeURL = curriculumVitaeURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
