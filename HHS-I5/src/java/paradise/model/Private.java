package paradise.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Groep3
*/
@Entity
@Table(name = "Private")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Private.findAll", query = "SELECT u FROM Private u")
    })
public class Private implements Serializable {

    public Private() {
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Private(int customer, String phone, String firstName, String surName, String state) {
        this.customer = customer;
        this.phone = phone;
        this.firstName = firstName;
        this.surName = surName;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "customer")
    private int customer;

    @NotNull
    @Column(name = "phone")
    private String phone;
    
    @NotNull
    @Column(name = "firstName")
    private String firstName;
    
    @NotNull
    @Column(name = "surName")
    private String surName;
    
    @NotNull
    @Column(name = "state")
    private String state;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Booking> bookingList;

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

}
