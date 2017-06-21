/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Components.Repositories.EventDao;
import Components.Repositories.PlanDao;
import Entities.Event;
import Entities.Plan;
import Enums.Messages.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("planService")
public class PlanService {
    
    @Autowired
    public EventDao eventService;
    
    @Autowired
    public PlanDao planDao;
    
    @Autowired
    public UserService userService;
    
    public Plan getPlanById(Long id){
        return (Plan) planDao.getById(id);
    }
    
    public List<Plan> getPlans(HttpHeaders headers){
        return userService.getByHeader(headers).getPlans();
    }
    
    public List<Plan> getPlansByDate(HttpHeaders headers, Date date){
        List<Plan> list = userService.getByHeader(headers).getPlans();
        Calendar wantedDate = Calendar.getInstance();
        wantedDate.setTime(date);
        wantedDate.set(Calendar.HOUR, 0);
        wantedDate.set(Calendar.MINUTE, 0);
        wantedDate.set(Calendar.SECOND, 0);
        wantedDate.set(Calendar.MILLISECOND, 0);
        for(int index = 0; index < list.size(); index++){
            Calendar givenDate = Calendar.getInstance();
            givenDate.setTime(list.get(index).getDay());
            givenDate.set(Calendar.HOUR, 0);
            givenDate.set(Calendar.MINUTE, 0);
            givenDate.set(Calendar.SECOND, 0);
            givenDate.set(Calendar.MILLISECOND, 0);
            if(!wantedDate.equals(givenDate)){
                list.remove(index);
                index--;
            }
        }
        return list;
    }
    
    public List<Plan> getPlansByDate(Calendar date){
        return planDao.getPlanByDate(date.getTime());
    }
    
    public boolean isEventFitForPlan(Event event, Plan plan){
        Calendar wantedDate = Calendar.getInstance();
        wantedDate.setTime(event.getDate());
        wantedDate.set(Calendar.HOUR, 0);
        wantedDate.set(Calendar.MINUTE, 0);
        wantedDate.set(Calendar.SECOND, 0);
        wantedDate.set(Calendar.MILLISECOND, 0);
        wantedDate.set(Calendar.AM_PM, Calendar.AM);
        Calendar givenDate = Calendar.getInstance();
        givenDate.setTime(plan.getDay());
        givenDate.set(Calendar.HOUR, 0);
        givenDate.set(Calendar.MINUTE, 0);
        givenDate.set(Calendar.SECOND, 0);
        givenDate.set(Calendar.MILLISECOND, 0);
        Date a = wantedDate.getTime();
        Date b = givenDate.getTime();
        return wantedDate.equals(givenDate);
    }
    
    public Response savePlan(Plan plan){
        boolean succes = planDao.save(plan);
        if(succes){
            return Response.SUCCESFULL_POST;
        }
        return Response.UNSUCCCESFULL_POST;
    }
    
    public Response updatePlan(Plan plan){
        boolean succes = planDao.update(plan);
        if(succes){
            return Response.SUCCESFULL_PUT;
        }
        return Response.UNSUCCESFULL_PUT;
    }
    
    public Response deletePlanEvents(String email, Event event){
        boolean succes = planDao.deleteEventFromPlans(email, event);
        if(succes){
            return Response.SUCCESFULL_DELETE;
        }
        return Response.UNSUCCESFULL_DELETE;
    }
}
