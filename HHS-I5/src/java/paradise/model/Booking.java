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
@Table(name = "Booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT u FROM Booking u")
    })
public class Booking implements Serializable {

    public static final double CANCELLATION_INSURANCE_PRICE = 30.00;

    public Booking() {
    }

    public Booking(int ID, int amountOfKids, int amountOfAdults, boolean hasCancellationInsurance, int salePrice, Private customer, Trip trip) {
        this.ID = ID;
        this.amountOfKids = amountOfKids;
        this.amountOfAdults = amountOfAdults;
        this.hasCancellationInsurance = hasCancellationInsurance;
        this.salePrice = salePrice;
        this.customer = customer;
        this.trip = trip;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAmountOfAdults() {
        return amountOfAdults;
    }

    public void setAmountOfAdults(int amountOfAdults) {
        this.amountOfAdults = amountOfAdults;
    }

    public int getAmountOfKids() {
        return amountOfKids;
    }

    public void setAmountOfKids(int amountOfKids) {
        this.amountOfKids = amountOfKids;
    }

    public Private getCustomer() {
        return customer;
    }

    public void setCustomer(Private customer) {
        this.customer = customer;
    }

    public boolean isHasCancellationInsurance() {
        return hasCancellationInsurance;
    }

    public void setHasCancellationInsurance(boolean hasCancellationInsurance) {
        this.hasCancellationInsurance = hasCancellationInsurance;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "ID")
    private int ID;
    
    @NotNull
    @Column(name = "amountOfKids")
    private int amountOfKids;

    @NotNull
    @Column(name = "amountOfAdults")
    private int amountOfAdults;

    @NotNull
    @Column(name = "hasCancellationInsurance")
    private boolean hasCancellationInsurance;

    @NotNull
    @Column(name = "salePrice")
    private int salePrice;

    @NotNull
    @ManyToOne
    private Private customer;

    @NotNull
    @ManyToOne
    private Trip trip;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookingExcursion> bookingExcursionList;

    public List<BookingExcursion> getBookingExcursionList() {
        return bookingExcursionList;
    }

    public void setBookingExcursionList(List<BookingExcursion> bookingExcursionList) {
        this.bookingExcursionList = bookingExcursionList;
    }

}
