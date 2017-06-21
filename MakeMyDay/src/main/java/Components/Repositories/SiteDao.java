/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.EventType;
import Entities.SiteTemplate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dragos
 */
@Repository
@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class SiteDao extends EntityDao{
    
    @Transactional
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(SiteTemplate.class, id);
    }
    
    @Transactional
    public List<SiteTemplate> getAllSites(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SiteTemplate> query = entityManager.createNamedQuery("SiteTemplate.findAll", SiteTemplate.class);
        return query.getResultList();
    }
    
    @Transactional
    public List<SiteTemplate> getByName(String name){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SiteTemplate> query = entityManager.createNamedQuery("SiteTemplate.findByName", SiteTemplate.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
    
    @Transactional
    @Override
    public boolean save(DBEntity object) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        SiteTemplate site = (SiteTemplate) object;
        site.setContentType(entityManager.find(EventType.class, site.getContentType().getId()));
        entityManager.persist(site);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }

    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        SiteTemplate newOriginal = (SiteTemplate) newObject;
        SiteTemplate original = entityManager.find(SiteTemplate.class, newOriginal.getId());
        original.setName(newOriginal.getName());
        original.setLink(newOriginal.getLink());
        original.setEventUrlType(newOriginal.getEventUrlType());
        original.setContentType(newOriginal.getContentType());
        original.setDateFormat(newOriginal.getDateFormat());
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
}
