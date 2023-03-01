/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
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
public class KeyWordDao extends EntityDao {

    @Transactional
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(KeyWord.class, id);
    }

    @Transactional
    public List<KeyWord> getAllKeyWords() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<KeyWord> query = entityManager.createNamedQuery("KeyWord.findAll", KeyWord.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public boolean update(DBEntity newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

}
