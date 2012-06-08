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
@Table(name = "triptype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TripType.findAll", query = "SELECT t FROM TripType t"),
    @NamedQuery(name = "TripType.findById", query = "SELECT t FROM TripType t WHERE t.id = :id"),
    @NamedQuery(name = "TripType.findByMaxAmountOfPeople", query = "SELECT t FROM TripType t WHERE t.maxAmountOfPeople = :maxAmountOfPeople"),
    @NamedQuery(name = "TripType.findByKidsAllowed", query = "SELECT t FROM TripType t WHERE t.kidsAllowed = :kidsAllowed")})
public class TripType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maxAmountOfPeople")
    private int maxAmountOfPeople;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kidsAllowed")
    private boolean kidsAllowed;
    @ManyToMany(mappedBy = "tripTypeList", fetch = FetchType.LAZY)
    private List<Product> productList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripType", fetch = FetchType.LAZY)
    private List<ExcursionType> excursionTypeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripType", fetch = FetchType.LAZY)
    private List<Trip> tripList;

    public TripType() {
    }

    public TripType(Integer id) {
        this.id = id;
    }

    public TripType(Integer id, String name, String description, int maxAmountOfPeople, boolean kidsAllowed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxAmountOfPeople = maxAmountOfPeople;
        this.kidsAllowed = kidsAllowed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxAmountOfPeople() {
        return maxAmountOfPeople;
    }

    public void setMaxAmountOfPeople(int maxAmountOfPeople) {
        this.maxAmountOfPeople = maxAmountOfPeople;
    }

    public boolean getKidsAllowed() {
        return kidsAllowed;
    }

    public void setKidsAllowed(boolean kidsAllowed) {
        this.kidsAllowed = kidsAllowed;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @XmlTransient
    public List<ExcursionType> getExcursionTypeList() {
        return excursionTypeList;
    }

    public void setExcursionTypeList(List<ExcursionType> excursionTypeList) {
        this.excursionTypeList = excursionTypeList;
    }

    @XmlTransient
    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
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
        if (!(object instanceof TripType)) {
            return false;
        }
        TripType other = (TripType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paradise.dbmodel.TripType[ id=" + id + " ]";
    }
    
}
