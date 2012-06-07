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
@Table(name = "Product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT u FROM Product u")
    })
public class Product implements Serializable {

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Product() {
    }

    public Product(int ID, String productName) {
        this.ID = ID;
        this.productName = productName;
    }
 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "ID")
    private int ID;
    
    @NotNull
    @Column(name = "productName")
    private String productName;

    @ManyToMany
    @JoinTable(name="TripTypeProduct",
      joinColumns={@JoinColumn(name="product", referencedColumnName="ID")},
      inverseJoinColumns={@JoinColumn(name="tripType", referencedColumnName="ID")})
    private List<TripType> relatedTripTypes;

    public List<TripType> getRelatedTripTypes() {
        return relatedTripTypes;
    }

    public void setRelatedTripTypes(List<TripType> relatedTripTypes) {
        this.relatedTripTypes = relatedTripTypes;
    }

    // -- Overige attributen weggelaten; niet nodig voor de webshop demo

}
