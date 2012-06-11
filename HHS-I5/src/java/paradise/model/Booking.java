/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Philipp
 */
@Entity
@Table(name = "booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.findByAmountOfAdults", query = "SELECT b FROM Booking b WHERE b.amountOfAdults = :amountOfAdults"),
    @NamedQuery(name = "Booking.findByAmountOfKids", query = "SELECT b FROM Booking b WHERE b.amountOfKids = :amountOfKids"),
    @NamedQuery(name = "Booking.findByHasCancellationInsurance", query = "SELECT b FROM Booking b WHERE b.hasCancellationInsurance = :hasCancellationInsurance"),
    @NamedQuery(name = "Booking.findBySalePrice", query = "SELECT b FROM Booking b WHERE b.salePrice = :salePrice")})
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final double CANCELLATION_INSURANCE_PRICE = 30.00;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amountOfAdults")
    private short amountOfAdults;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amountOfKids")
    private short amountOfKids;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hasCancellationInsurance")
    private boolean hasCancellationInsurance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "salePrice")
    private double salePrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking1", fetch = FetchType.LAZY)
    private List<BookingExcursion> bookingExcursionList;
    @JoinColumn(name = "trip", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Trip trip;
    @JoinColumn(name = "private", referencedColumnName = "customer")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Private private1;

    public Booking() {
    }

    public Booking(Integer id) {
        this.id = id;
    }

    public Booking(Integer id, short amountOfAdults, short amountOfKids, boolean hasCancellationInsurance, double salePrice) {
        this.id = id;
        this.amountOfAdults = amountOfAdults;
        this.amountOfKids = amountOfKids;
        this.hasCancellationInsurance = hasCancellationInsurance;
        this.salePrice = salePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getAmountOfAdults() {
        return amountOfAdults;
    }

    public void setAmountOfAdults(short amountOfAdults) {
        this.amountOfAdults = amountOfAdults;
    }

    public short getAmountOfKids() {
        return amountOfKids;
    }

    public void setAmountOfKids(short amountOfKids) {
        this.amountOfKids = amountOfKids;
    }

    public boolean getHasCancellationInsurance() {
        return hasCancellationInsurance;
    }

    public void setHasCancellationInsurance(boolean hasCancellationInsurance) {
        this.hasCancellationInsurance = hasCancellationInsurance;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @XmlTransient
    public List<BookingExcursion> getBookingExcursionList() {
        return bookingExcursionList;
    }

    public void setBookingExcursionList(List<BookingExcursion> bookingExcursionList) {
        this.bookingExcursionList = bookingExcursionList;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Private getPrivate1() {
        return private1;
    }

    public void setPrivate1(Private private1) {
        this.private1 = private1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paradise.dbmodel.Booking[ id=" + id + " ]";
    }
    
}
