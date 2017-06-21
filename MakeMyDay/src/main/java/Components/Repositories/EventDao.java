/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.Event;
import Entities.EventType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dragos
 */
@Repository
@Component
public class EventDao extends EntityDao{
    
    @Transactional
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Event.class, id);
    }
    
    @Transactional
    public List<Event> getAllEvents(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Event> query = entityManager.createNamedQuery("Event.findAll", Event.class);
        return query.getResultList();
    }
    
    @Transactional
    public List<Event> getAllDetermentEvents(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Event> query = entityManager.createNamedQuery("Event.findDetermined", Event.class);
        return query.getResultList();
    }
    
    
    @Transactional
    public Event getEventById(Long id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Event> query = entityManager.createNamedQuery("Event.findById", Event.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
    
    @Transactional
    public List<Event> getEventByName(String name){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Event> query = entityManager.createNamedQuery("Event.findByName", Event.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
    
    @Transactional
    public List<Event> getEventByType(EventType type){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Event> query = entityManager.createNamedQuery("Event.findByType", Event.class);
        query.setParameter("type", type.getType());
        return query.getResultList();
    }
    
    @Transactional
    public List<Event> getEventByFilter(EventType type, Date start, Date finish){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Event> query;
        if(type != null){
            query = entityManager.createNamedQuery("Event.findByFilter", Event.class);
            query.setParameter("type", type);
        }else{
            query = entityManager.createNamedQuery("Event.findByFilterAny", Event.class);
        }
        query.setParameter("start", start);
        query.setParameter("finish", finish);
        return query.getResultList();
    }
    
    @Transactional
    public boolean deleteAllEvents(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("DELETE FROM Plan p where 1 = 1");
        int result = query.executeUpdate();
        if(result > 0){
            Logger.getLogger(EventDao.class).log(Logger.Level.INFO, "DELETED ALL PLANS");
        }
        query = entityManager.createQuery("DELETE FROM Event e where 1 = 1");
        result = query.executeUpdate();
        if(result > 0){
            Logger.getLogger(EventDao.class).log(Logger.Level.INFO, "DELETED ALL EVENTS");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result > 0;
    }
    
    @Transactional
    public boolean deleteAllExpiredEvents(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Query query = entityManager.createQuery("DELETE FROM Event e where e.date < :lastMonth");
        query.setParameter("lastMonth", calendar.getTime(), TemporalType.DATE);
        int result = query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result > 0;
    }

    @Override
    public boolean update(DBEntity newObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Transactional
    @Override
    public boolean save(DBEntity object){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            Event event = (Event) object;
            event.setType(entityManager.find(EventType.class, event.getType().getId()));
            entityManager.persist(event);
            entityManager.getTransaction().commit();
            entityManager.close();
        }catch(Exception ex){
            Logger.getLogger(EventDao.class).log(Logger.Level.WARN, ex);
            entityManager.getTransaction().rollback();
            entityManager.close();
            return false;
        }
        return true;
    }
    
}
