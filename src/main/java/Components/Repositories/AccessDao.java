/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.AccessLog;
import Entities.DBEntity;
import Entities.User;
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
public class AccessDao extends EntityDao{

    @Transactional
    @Override
    public AccessLog getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(AccessLog.class, id);
    }
    
    @Transactional
    public AccessLog getByUser(User user){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<AccessLog> query = entityManager.createNamedQuery("AccessLog.findByUser", AccessLog.class);
        query.setParameter("user", user);
        try{
            AccessLog accessLog = query.getSingleResult();
            return accessLog;
        }catch(NoResultException ex){
            return null;
        }
    }

    @Transactional
    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        AccessLog newOriginal = (AccessLog) newObject;
        AccessLog original = entityManager.find(AccessLog.class, newOriginal.getId());
        original.setId(newOriginal.getId());
        original.setToken(newOriginal.getToken());
        original.setExpirationTime(newOriginal.getExpirationTime());
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
    @Transactional
    @Override
    public boolean save(DBEntity object) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        AccessLog accessLog = (AccessLog) object;
        accessLog.setUser(entityManager.find(User.class, accessLog.getUser().getId()));
        entityManager.persist(accessLog);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
}
