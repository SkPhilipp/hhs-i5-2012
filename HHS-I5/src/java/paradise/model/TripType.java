package paradise.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Groep3
 */
@Entity
@Table(name = "TripType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TripType.findAll", query = "SELECT u FROM TripType u")
    })
public class TripType  implements Serializable {

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<Excursion> getExcursionList() {
        return excursionList;
    }

    public void setExcursionList(List<Excursion> excursionList) {
        this.excursionList = excursionList;
    }

    public boolean getKidsAllowed() {
        return kidsAllowed;
    }

    public void setKidsAllowed(boolean kidsAllowed) {
        this.kidsAllowed = kidsAllowed;
    }

    public int getMaxAmountOfPeople() {
        return maxAmountOfPeople;
    }

    public void setMaxAmountOfPeople(int maxAmountOfPeople) {
        this.maxAmountOfPeople = maxAmountOfPeople;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }

    public TripType() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TripType(int ID, int maxAmountOfPeople, boolean kidsAllowed, String name, String description) {
        this.ID = ID;
        this.maxAmountOfPeople = maxAmountOfPeople;
        this.kidsAllowed = kidsAllowed;
        this.tripList = new ArrayList<Trip>();
        this.excursionList = new ArrayList<Excursion>();
        this.relatedProducts = new ArrayList<Product>();
        this.name = name;
        this.description = description;
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
    @Column(name = "kidsAllowed")
    private boolean kidsAllowed;
    
    @NotNull
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripType")
    private List<Trip> tripList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripType")
    private List<Excursion> excursionList;

    @ManyToMany
    @JoinTable(name="TripTypeProduct",
      joinColumns={@JoinColumn(name="tripType", referencedColumnName="ID")},
      inverseJoinColumns={@JoinColumn(name="product", referencedColumnName="ID")})
    private List<Product> relatedProducts;

}
