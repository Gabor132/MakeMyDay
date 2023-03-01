/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.EventType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class EventTypeDao extends EntityDao {

    @Transactional
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(EventType.class, id);
    }

    @Transactional
    public List<EventType> getAllEventTypes() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<EventType> query = entityManager.createNamedQuery("EventType.findAll", EventType.class);
        List<EventType> list = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return list;
    }

    @Transactional
    public EventType getEventByType(String typeValue) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<EventType> query = entityManager.createNamedQuery("EventType.findByType", EventType.class);
        query.setParameter("type", typeValue);
        EventType result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException ex) {
        }
        return result;
    }

    @Override
    public boolean update(DBEntity newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

}
