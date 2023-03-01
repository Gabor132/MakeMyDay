/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.ItemClass;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dragos
 */
@Repository
@Component
public class ItemClassDao extends EntityDao {

    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(ItemClass.class, id);
    }

    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ItemClass newOriginal = (ItemClass) newObject;
        ItemClass original = entityManager.find(ItemClass.class, newOriginal.getId());
        original.setClassName(newOriginal.getClassName());
        original.setClassType(newOriginal.getClassType());
        original.setValueLocation(newOriginal.getValueLocation());
        original.setValueType(newOriginal.getValueType());
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }

}
