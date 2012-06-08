/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Philipp
 */
@Entity
@Table(name = "bookingexcursion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingExcursion.findAll", query = "SELECT b FROM BookingExcursion b"),
    @NamedQuery(name = "BookingExcursion.findByBooking", query = "SELECT b FROM BookingExcursion b WHERE b.bookingExcursionPK.booking = :booking"),
    @NamedQuery(name = "BookingExcursion.findByExcursion", query = "SELECT b FROM BookingExcursion b WHERE b.bookingExcursionPK.excursion = :excursion"),
    @NamedQuery(name = "BookingExcursion.findByAmountOfPeople", query = "SELECT b FROM BookingExcursion b WHERE b.amountOfPeople = :amountOfPeople")})
public class BookingExcursion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BookingExcursionPK bookingExcursionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amountOfPeople")
    private int amountOfPeople;
    @JoinColumn(name = "booking", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Booking booking1;
    @JoinColumn(name = "excursion", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Excursion excursion1;

    public BookingExcursion() {
    }

    public BookingExcursion(BookingExcursionPK bookingExcursionPK) {
        this.bookingExcursionPK = bookingExcursionPK;
    }

    public BookingExcursion(BookingExcursionPK bookingExcursionPK, int amountOfPeople) {
        this.bookingExcursionPK = bookingExcursionPK;
        this.amountOfPeople = amountOfPeople;
    }

    public BookingExcursion(int booking, int excursion) {
        this.bookingExcursionPK = new BookingExcursionPK(booking, excursion);
    }

    public BookingExcursionPK getBookingExcursionPK() {
        return bookingExcursionPK;
    }

    public void setBookingExcursionPK(BookingExcursionPK bookingExcursionPK) {
        this.bookingExcursionPK = bookingExcursionPK;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public Booking getBooking1() {
        return booking1;
    }

    public void setBooking1(Booking booking1) {
        this.booking1 = booking1;
    }

    public Excursion getExcursion1() {
        return excursion1;
    }

    public void setExcursion1(Excursion excursion1) {
        this.excursion1 = excursion1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingExcursionPK != null ? bookingExcursionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookingExcursion)) {
            return false;
        }
        BookingExcursion other = (BookingExcursion) object;
        if ((this.bookingExcursionPK == null && other.bookingExcursionPK != null) || (this.bookingExcursionPK != null && !this.bookingExcursionPK.equals(other.bookingExcursionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paradise.dbmodel.BookingExcursion[ bookingExcursionPK=" + bookingExcursionPK + " ]";
    }
    
}
