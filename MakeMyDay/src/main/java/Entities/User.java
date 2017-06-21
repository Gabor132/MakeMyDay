/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Enums.UserType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dragos
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
    @NamedQuery(name="User.findAll", query = "SELECT u FROM User u WHERE u.type != 'UNCONFIRMED'"),
    @NamedQuery(name="User.findById", query = "SELECT u FROM User u WHERE u.id = :id AND u.type != 'UNCONFIRMED'"),
    @NamedQuery(name="User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email AND u.type != 'UNCONFIRMED'"),
    @NamedQuery(name="User.findUnconfirmed", query = "SELECT u FROM User u WHERE u.type = 'UNCONFIRMED'")
})
public class User implements Serializable, DBEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    
    @Column(name = "EMAIL", unique = true)
    private String email;
    
    @Column(name = "PASSWORD")
    private byte[] password;
    
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Plan> plans;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "PREFERENCES",
        joinColumns = @JoinColumn(
                name = "USER_ID",
                referencedColumnName = "ID"
        ),
        inverseJoinColumns = @JoinColumn(
                name = "EVENT_TYPE",
                referencedColumnName = "ID"
        )
    )
    private Set<EventType> preferences = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name="TYPE")
    @NotNull
    private UserType type;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<AccessLog> accessLogs;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }
    
    public UserType getType(){
        return type;
    }
    
    public void setType(UserType type){
        this.type = type;
    }

    public Set<EventType> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<EventType> preferences) {
        this.preferences = preferences;
    }

    public List<AccessLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(List<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.email);
        hash = 13 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
