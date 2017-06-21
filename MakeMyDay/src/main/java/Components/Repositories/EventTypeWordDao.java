/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.EventType;
import Entities.EventTypeWord;
import Entities.KeyWord;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dragos
 */
@Repository
@Component
public class EventTypeWordDao extends EntityDao{

    @Deprecated
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(KeyWord.class, id);
    }
    
    public EventTypeWord getByPrimaryKey(EventType type, KeyWord word){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<EventTypeWord> query = entityManager.createNamedQuery("EventTypeWord.findByPrimaryKey", EventTypeWord.class);
        query.setParameter("type", type);
        query.setParameter("word", word);
        return query.getSingleResult();
    }
    
    @Transactional
    public List<EventTypeWord> getAllEventTypeWord(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<EventTypeWord> query = entityManager.createNamedQuery("EventTypeWord.findAll", EventTypeWord.class);
        return query.getResultList();
    }
    
    @Transactional
    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        EventTypeWord newOriginal = (EventTypeWord) newObject;
        TypedQuery<EventTypeWord> query = entityManager.createNamedQuery("EventTypeWord.findByPrimaryKey", EventTypeWord.class);
        query.setParameter("type", newOriginal.getType());
        query.setParameter("word", newOriginal.getWord());
        EventTypeWord original = query.getSingleResult();
        original.setPercentage(newOriginal.getPercentage());
        entityManager.merge(original);
        entityManager.getTransaction().commit();
        return true;
    }
    
}
