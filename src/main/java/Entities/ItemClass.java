/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Enums.ClassType;
import Enums.ValueType;
import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dragos
 */
@Entity
@Table(name = "ITEM_CLASSES")
public class ItemClass implements Serializable, DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CLASS_NAME")
    @NotNull
    private String className;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLASS_TYPE")
    @NotNull
    private ClassType classType;

    @Enumerated(EnumType.STRING)
    @Column(name = "VALUE_TYPE")
    @NotNull
    private ValueType valueType;

    @Column(name = "VALUE_LOCATION")
    private String valueLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private ItemTemplate hostItem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<ItemClass> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CLASS_ID")
    private ItemClass parent;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public Set<ItemClass> getChildren() {
        return children;
    }

    public void setChildren(Set<ItemClass> children) {
        this.children = children;
    }

    public ItemClass getParent() {
        return parent;
    }

    public void setParent(ItemClass parent) {
        this.parent = parent;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getValueLocation() {
        return valueLocation;
    }

    public void setValueLocation(String valueLocation) {
        this.valueLocation = valueLocation;
    }

    public ItemTemplate getHostItem() {
        return hostItem;
    }

    public void setHostItem(ItemTemplate hostItem) {
        this.hostItem = hostItem;
        for (ItemClass child : children) {
            child.setHostItem(hostItem);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
