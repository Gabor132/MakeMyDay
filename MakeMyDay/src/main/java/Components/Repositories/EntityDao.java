/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dragos
 */
@Repository
@Component
public abstract class EntityDao {
    
    @PersistenceUnit
    public EntityManagerFactory entityManagerFactory;
    
    public abstract DBEntity getById(Long id);
    
    @Transactional
    public boolean save(DBEntity object) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        if(entityManager.contains(object)){
            entityManager.merge(object);
        }else{
            entityManager.persist(object);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
    @Transactional
    public abstract boolean update(DBEntity newObject);
    
    @Transactional
    public boolean delete(Class targetClass, long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DBEntity object = (DBEntity) entityManager.find(targetClass,id);
        entityManager.getTransaction().begin();
        entityManager.refresh(object);
        entityManager.remove(object);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
}
