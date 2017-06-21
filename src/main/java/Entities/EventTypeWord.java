/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dragos
 */
@Entity
@Table(name = "EVENT_TYPE_WORDS")
@NamedQueries({
    @NamedQuery(name = "EventTypeWord.findAll", query = "SELECT etk FROM EventTypeWord etk"),
    @NamedQuery(name = "EventTypeWord.findByPrimaryKey", query = "SELECT etk FROM EventTypeWord etk WHERE etk.type = :type AND etk.word = :word")
})
public class EventTypeWord implements DBEntity, Serializable {
    
    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "EVENT_TYPE_ID")
    private EventType type;
    
    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "KEY_WORD_ID")
    private KeyWord word;
    
    @Column(name = "PERCENTAGE")
    private double percentage;

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public KeyWord getWord() {
        return word;
    }

    public void setWord(KeyWord word) {
        this.word = word;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.type);
        hash = 17 * hash + Objects.hashCode(this.word);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.percentage) ^ (Double.doubleToLongBits(this.percentage) >>> 32));
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
        final EventTypeWord other = (EventTypeWord) obj;
        if (Double.doubleToLongBits(this.percentage) != Double.doubleToLongBits(other.percentage)) {
            return false;
        }
        if (!Objects.equals(this.word, other.word)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }
    
    
    
}
