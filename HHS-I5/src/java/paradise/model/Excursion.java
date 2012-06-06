package paradise.model;

import java.io.Serializable;
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
    @Column(name = "price")
    private float price;

    @ManyToOne
    private TripType tripType;

}
