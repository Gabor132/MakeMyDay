/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Components.Repositories.EventTypeDao;
import Entities.EventType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("eventTypeService")
public class EventTypeService {
    
    @Autowired
    public EventTypeDao eventTypeDao;
    
    public EventType getEventType(String value){
        return eventTypeDao.getEventByType(value);
    }
    
    public List<EventType> getAllEventTypes(){
        return eventTypeDao.getAllEventTypes();
    }
    
}
