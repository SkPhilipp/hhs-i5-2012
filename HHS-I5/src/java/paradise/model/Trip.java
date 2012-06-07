package paradise.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Groep3
 */
@Entity
@Table(name = "Trip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trip.findAll", query = "SELECT u FROM Trip u")
    })
public class Trip implements Serializable {

    public Trip() {
    }

    public Trip(int ID, Date startDate, Date endDate, TripType tripType) {
        this.ID = ID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripType = tripType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }
 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "ID")
    private int ID;
    
    @NotNull
    @Column(name = "startDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    
    @NotNull
    @Column(name = "endDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    private TripType tripType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    private List<Booking> bookingList;

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public int getRemainingCount(){
        int amount = this.getTripType().getMaxAmountOfPeople();
        if(this.bookingList != null){
            for(Booking booking : this.bookingList){
                amount -= ( booking.getAmountOfAdults() + booking.getAmountOfKids() );
            }
        }
        return amount;
    }

}
