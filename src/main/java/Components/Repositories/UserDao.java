/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.EventType;
import Entities.User;
import Enums.UserType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dragos
 */
@Repository
@Component
public class UserDao extends EntityDao{

    @Autowired
    public EventTypeDao eventTypeDao;
    
    @Transactional
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            return entityManager.find(User.class, id);
        }catch(NoResultException ex){
            return null;
        }
    }
    
    @Transactional
    public List<User> getAllUsers(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = entityManager.createNamedQuery("User.findAll", User.class);
        return query.getResultList();
    }
    
    @Transactional
    public User getUserByEmail(String email){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByEmail",User.class);
        query.setParameter("email", email);
        User user;
        try{
            user = query.getSingleResult();
            return user;
        }catch(NoResultException ex){
            return null;
        }
    }
    
    @Transactional
    @Override
    public boolean save(DBEntity object) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        User user = (User) object;
        if(entityManager.contains(object)){
            entityManager.merge(object);
        }else{
            TypedQuery<EventType> query = entityManager.createNamedQuery("EventType.findAll", EventType.class);
            List<EventType> list = query.getResultList();
            user.getPreferences().clear();
            user.getPreferences().addAll(list);
            entityManager.persist(user);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
    @Transactional
    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User newOriginal = (User) newObject;
        User original = entityManager.find(User.class, newOriginal.getId());
        entityManager.getTransaction().begin();
        original.setId(newOriginal.getId());
        original.setEmail(newOriginal.getEmail());
        original.setPassword(newOriginal.getPassword());
        original.setPlans(newOriginal.getPlans());
        original.getPreferences().clear();
        List<EventType> list = eventTypeDao.getAllEventTypes();
        for(int i = 0; i < list.size(); i++){
            if(!newOriginal.getPreferences().contains(list.get(i))){
                list.remove(i);
            }else{
                original.getPreferences().add(list.get(i));
            }
        }
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
    @Transactional
    public boolean updatePreference(String email, List<String> preferences) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByEmail",User.class);
        query.setParameter("email", email);
        User original;
        try{
            original = query.getSingleResult();
        }catch(NoResultException ex){
            return false;
        }
        original.getPreferences().clear();
        TypedQuery<EventType> query2 = entityManager.createNamedQuery("EventType.findAll", EventType.class);
        List<EventType> list = query2.getResultList();
        for(EventType event : list){
            if(preferences.contains(event.getType())){
                original.getPreferences().add(event);
            }
        }
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
    @Transactional
    public boolean confirmUser(Long id, boolean isAdmin){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        User user = entityManager.find(User.class, id);
        user.setType(isAdmin?UserType.ADMIN:UserType.NORMAL);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        return true;
    }
    
    @Transactional
    public List<User> getUnconfirmedUsers(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = entityManager.createNamedQuery("User.findUnconfirmed", User.class);
        return query.getResultList();
    }
    
}
