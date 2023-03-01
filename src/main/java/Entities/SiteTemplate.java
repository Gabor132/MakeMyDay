/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Enums.EventUrlType;
import java.io.Serializable;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dragos
 */
@Entity
@Table(name = "SITE_TEMPLATES")
@NamedQueries({ @NamedQuery(name = "SiteTemplate.findAll", query = "SELECT s FROM SiteTemplate s"),
        @NamedQuery(name = "SiteTemplate.findByName", query = "SELECT s FROM SiteTemplate s WHERE s.name = :name"),
        @NamedQuery(name = "SiteTemplate.deleteById", query = "DELETE FROM SiteTemplate s WHERE s.id = :id") })
public class SiteTemplate implements Serializable, DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "EVENT_URL_TYPE")
    @NotNull
    private EventUrlType eventUrlType;

    @ManyToOne()
    @JoinColumn(name = "CONTENT_TYPE")
    private EventType contentType;

    @Column(name = "LINK")
    @NotNull
    private String link;

    @Column(name = "DATE_FORMAT")
    @NotNull
    private String dateFormat;

    @OneToOne(mappedBy = "hostSite", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private ItemTemplate itemTemplate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hostSite", cascade = CascadeType.ALL)
    private List<Event> events;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventUrlType getEventUrlType() {
        return eventUrlType;
    }

    public void setEventUrlType(EventUrlType eventUrlType) {
        this.eventUrlType = eventUrlType;
    }

    public String getLink() {
        return link;
    }

    public EventType getContentType() {
        return contentType;
    }

    public void setContentType(EventType contentType) {
        this.contentType = contentType;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public ItemTemplate getItemTemplate() {
        return itemTemplate;
    }

    public void setItemTemplate(ItemTemplate itemTemplate) {
        this.itemTemplate = itemTemplate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
