package paradise.model;

import java.io.Serializable;
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

}
