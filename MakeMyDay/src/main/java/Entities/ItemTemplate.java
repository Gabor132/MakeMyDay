/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Dragos
 */
@Entity
@Table(name="ITEM_TEMPLATES")
public class ItemTemplate implements Serializable, DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    
    /**
     * This might have to be the class name of the template root
     */
    @Column(name="NAME")
    private String name;

    @OneToOne
    @JoinColumn(name="SITE_ID")
    private SiteTemplate hostSite;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hostItem", cascade = CascadeType.ALL)
    private List<ItemClass> classes;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SiteTemplate getHostSite() {
        return hostSite;
    }

    public void setHostSite(SiteTemplate hostSite) {
        this.hostSite = hostSite;
    }

    public List<ItemClass> getClasses() {
        return classes;
    }

    public void setClasses(List<ItemClass> classes) {
        this.classes = classes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
