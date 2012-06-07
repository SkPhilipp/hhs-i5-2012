package paradise.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Groep3
 */
@Entity
@Table(name = "BookingExcursion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingExcursion.findAll", query = "SELECT u FROM BookingExcursion u")
    })
public class BookingExcursion implements Serializable {
    
    @Id
    @ManyToOne
    private Booking booking;

    @Id
    @ManyToOne
    private Excursion excursion;

    @NotNull
    @Column(name = "amountOfPeople")
    private int amountOfPeople;

    public BookingExcursion() {
    }

    public BookingExcursion(Booking booking, Excursion excursion, int amountOfPeople) {
        this.booking = booking;
        this.excursion = excursion;
        this.amountOfPeople = amountOfPeople;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Excursion getExcursion() {
        return excursion;
    }

    public void setExcursion(Excursion excursion) {
        this.excursion = excursion;
    }

}
