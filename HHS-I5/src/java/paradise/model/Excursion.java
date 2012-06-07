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
@Table(name = "Excursion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excursion.findAll", query = "SELECT u FROM Excursion u")
    })
public class Excursion  implements Serializable {

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public int getMaxAmountOfPeople() {
        return maxAmountOfPeople;
    }

    public void setMaxAmountOfPeople(int maxAmountOfPeople) {
        this.maxAmountOfPeople = maxAmountOfPeople;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public Excursion() {
    }

    public Excursion(int ID, int maxAmountOfPeople, String guide, double price, String name) {
        this.ID = ID;
        this.maxAmountOfPeople = maxAmountOfPeople;
        this.guide = guide;
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "ID")
    private int ID;
    
    @NotNull
    @Column(name = "maxAmountOfPeople")
    private int maxAmountOfPeople;
    
    @NotNull
    @Column(name = "guide")
    private String guide;

    @NotNull
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @ManyToOne
    private TripType tripType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "excursion")
    private List<BookingExcursion> bookingExcursionList;

    public List<BookingExcursion> getBookingExcursionList() {
        return bookingExcursionList;
    }

    public void setBookingExcursionList(List<BookingExcursion> bookingExcursionList) {
        this.bookingExcursionList = bookingExcursionList;
    }

}
