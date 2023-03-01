/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.EventTypeDao;
import DataToObjects.EventTypeDto;
import Entities.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service
@Component
public class EventTypeMapper {

    @Autowired
    public EventTypeDao eventTypeDao;

    public EventTypeDto toDto(EventType eventType) {
        EventTypeDto dto = new EventTypeDto();
        dto.type = eventType.getType();
        return dto;
    }

    public EventType toDomain(EventTypeDto dto) {
        return eventTypeDao.getEventByType(dto.type);
    }

}
