/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Philipp
 */
@Entity
@Table(name = "private")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Private.findAll", query = "SELECT p FROM Private p"),
    @NamedQuery(name = "Private.findByCustomer", query = "SELECT p FROM Private p WHERE p.customer = :customer")})
public class Private implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "customer")
    private Integer customer;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "firstName")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "surName")
    private String surName;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "state")
    private String state;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "private1", fetch = FetchType.LAZY)
    private List<Booking> bookingList;

    public Private() {
    }

    public Private(Integer customer) {
        this.customer = customer;
    }

    public Private(Integer customer, String phone, String firstName, String surName, String state) {
        this.customer = customer;
        this.phone = phone;
        this.firstName = firstName;
        this.surName = surName;
        this.state = state;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @XmlTransient
    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customer != null ? customer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Private)) {
            return false;
        }
        Private other = (Private) object;
        if ((this.customer == null && other.customer != null) || (this.customer != null && !this.customer.equals(other.customer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paradise.dbmodel.Private[ customer=" + customer + " ]";
    }
    
}
