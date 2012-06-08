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
@Table(name = "excursion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excursion.findAll", query = "SELECT e FROM Excursion e"),
    @NamedQuery(name = "Excursion.findById", query = "SELECT e FROM Excursion e WHERE e.id = :id"),
    @NamedQuery(name = "Excursion.findByMaxAmountOfPeople", query = "SELECT e FROM Excursion e WHERE e.maxAmountOfPeople = :maxAmountOfPeople"),
    @NamedQuery(name = "Excursion.findByPrice", query = "SELECT e FROM Excursion e WHERE e.price = :price")})
public class Excursion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maxAmountOfPeople")
    private int maxAmountOfPeople;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "guide")
    private String guide;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "excursion1", fetch = FetchType.LAZY)
    private List<BookingExcursion> bookingExcursionList;
    @JoinColumn(name = "excursionType", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExcursionType excursionType;
    @JoinColumn(name = "trip", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Trip trip;

    public Excursion() {
    }

    public Excursion(Integer id) {
        this.id = id;
    }

    public Excursion(Integer id, int maxAmountOfPeople, String guide, double price) {
        this.id = id;
        this.maxAmountOfPeople = maxAmountOfPeople;
        this.guide = guide;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMaxAmountOfPeople() {
        return maxAmountOfPeople;
    }

    public void setMaxAmountOfPeople(int maxAmountOfPeople) {
        this.maxAmountOfPeople = maxAmountOfPeople;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @XmlTransient
    public List<BookingExcursion> getBookingExcursionList() {
        return bookingExcursionList;
    }

    public void setBookingExcursionList(List<BookingExcursion> bookingExcursionList) {
        this.bookingExcursionList = bookingExcursionList;
    }

    public ExcursionType getExcursionType() {
        return excursionType;
    }

    public void setExcursionType(ExcursionType excursionType) {
        this.excursionType = excursionType;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
        if (!(object instanceof Excursion)) {
            return false;
        }
        Excursion other = (Excursion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paradise.dbmodel.Excursion[ id=" + id + " ]";
    }
    
    public int getRemainingCount(){
        int amount = this.getMaxAmountOfPeople();
        if(this.bookingExcursionList != null){
            for(BookingExcursion bookingexcursion : this.bookingExcursionList){
                amount -= bookingexcursion.getAmountOfPeople();
            }
        }
        return amount;
    }

}
