/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dragos
 */
@Entity
@Table(name = "EVENTS")
@NamedQueries({ @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
        @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
        @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"),
        @NamedQuery(name = "Event.findByType", query = "SELECT e FROM Event e WHERE e.type = :type"),
        @NamedQuery(name = "Event.findDetermined", query = "SELECT e FROM Event e WHERE e.type.id != (SELECT et.id FROM EventType et WHERE et.type = 'ALTELE')"),
        @NamedQuery(name = "Event.findByFilter", query = "SELECT e FROM Event e WHERE"
                + " e.type = :type AND e.date BETWEEN :start AND :finish"),
        @NamedQuery(name = "Event.findByFilterAny", query = "SELECT e FROM Event e WHERE"
                + " e.date BETWEEN :start AND :finish") })
public class Event implements Serializable, DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(cascade = CascadeType.DETACH, targetEntity = EventType.class)
    @JoinColumn(name = "TYPE_ID")
    private EventType type;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "DATE_EVENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LINK")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private SiteTemplate hostSite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SiteTemplate getHostSite() {
        return hostSite;
    }

    public void setHostSite(SiteTemplate hostSite) {
        this.hostSite = hostSite;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.name);
        hash = 73 * hash + Objects.hashCode(this.type);
        hash = 73 * hash + Objects.hashCode(this.date);
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
        final Event other = (Event) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return Objects.equals(this.date, other.date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUsable() {
        return !this.name.isEmpty() && !this.address.isEmpty() && !this.imageUrl.isEmpty();
    }

    public String getContent() {
        String eventContent = this.name + " " + this.address + " " + this.description;
        return eventContent.toUpperCase();
    }

}
