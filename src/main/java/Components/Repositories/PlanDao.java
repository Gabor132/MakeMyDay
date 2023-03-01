/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Repositories;

import Entities.DBEntity;
import Entities.Event;
import Entities.Plan;
import Entities.User;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
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
public class PlanDao extends EntityDao {

    @Transactional
    @Override
    public DBEntity getById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Plan.class, id);
    }

    @Transactional
    public List<Plan> getPlanByUser(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Plan> query = entityManager.createNamedQuery("Plan.findByUser", Plan.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Transactional
    public List<Plan> getPlanByDate(Date date) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Plan> query = entityManager.createNamedQuery("Plan.findByDate", Plan.class);
        query.setParameter("day", date);
        return query.getResultList();
    }

    @Transactional
    @Override
    public boolean save(DBEntity object) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        if (entityManager.contains(object)) {
            entityManager.merge(object);
        } else {
            Plan plan = (Plan) object;
            Event event = entityManager.find(Event.class, ((Event) (plan.getEvents().toArray())[0]).getId());
            plan.setEvents(new HashSet<>(Arrays.asList(event)));
            entityManager.persist(plan);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }

    @Transactional
    @Override
    public boolean update(DBEntity newObject) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Plan newOriginal = (Plan) newObject;
        Plan original = entityManager.find(Plan.class, newOriginal.getId());
        original.setId(newOriginal.getId());
        original.getEvents().clear();
        for (Event event : newOriginal.getEvents()) {
            Event event2 = entityManager.find(Event.class, event.getId());
            original.getEvents().add(event2);
        }
        entityManager.persist(original);
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }

    @Transactional
    public boolean deleteEventFromPlans(String email, Event event) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        User original;
        try {
            original = query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        List<Plan> plans = original.getPlans();
        List<Plan> toBeDeleted = new LinkedList<>();
        for (int index = 0; index < plans.size(); index++) {
            if (plans.get(index).getEvents().contains(event)) {
                plans.get(index).getEvents().remove(event);
                if (plans.get(index).getEvents().isEmpty()) {
                    toBeDeleted.add(original.getPlans().remove(index));
                    break;
                }
            }
        }
        entityManager.merge(original);
        for (Plan plan : toBeDeleted) {
            entityManager.remove(plan);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return true;
    }

}
