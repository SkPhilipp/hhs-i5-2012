package paradise.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Groep3
*/
@Entity
@Table(name = "Private")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Private.findAll", query = "SELECT u FROM Private u")
    })
public class Private implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "customer")
    private int customer;

    @NotNull
    @Column(name = "phone")
    private String phone;
    
    @NotNull
    @Column(name = "firstName")
    private String firstName;
    
    @NotNull
    @Column(name = "surName")
    private String surName;
    
    @NotNull
    @Column(name = "state")
    private String state;

}
