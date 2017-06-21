/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.ItemTemplate;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dragos
 */
@Repository
@Component
public class ItemTemplateDao extends EntityDao{

    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(ItemTemplate.class, id);
    }

    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ItemTemplate newOriginal = (ItemTemplate) newObject;
        ItemTemplate original = entityManager.find(ItemTemplate.class, newOriginal.getId());
        original.setName(newOriginal.getName());
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }
    
}
