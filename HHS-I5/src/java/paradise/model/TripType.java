package paradise.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "TripType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TripType.findAll", query = "SELECT u FROM TripType u")
    })
public class TripType  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private int ID;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "maxAmountOfPeople")
    private int maxAmountOfPeople;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "kidsAllowed")
    private int kidsAllowed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripType")
    private List<Trip> tripList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripType")
    private List<Excursion> excursionList;

    //TODO: Products - many to many

}
