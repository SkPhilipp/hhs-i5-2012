/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Philipp
 */
@Embeddable
public class BookingExcursionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "booking")
    private int booking;
    @Basic(optional = false)
    @NotNull
    @Column(name = "excursion")
    private int excursion;

    public BookingExcursionPK() {
    }

    public BookingExcursionPK(int booking, int excursion) {
        this.booking = booking;
        this.excursion = excursion;
    }

    public int getBooking() {
        return booking;
    }

    public void setBooking(int booking) {
        this.booking = booking;
    }

    public int getExcursion() {
        return excursion;
    }

    public void setExcursion(int excursion) {
        this.excursion = excursion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) booking;
        hash += (int) excursion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookingExcursionPK)) {
            return false;
        }
        BookingExcursionPK other = (BookingExcursionPK) object;
        if (this.booking != other.booking) {
            return false;
        }
        if (this.excursion != other.excursion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paradise.dbmodel.BookingExcursionPK[ booking=" + booking + ", excursion=" + excursion + " ]";
    }
    
}
