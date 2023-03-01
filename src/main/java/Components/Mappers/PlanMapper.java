/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.PlanDao;
import DataToObjects.EventDto;
import DataToObjects.PlanDto;
import Entities.Event;
import Entities.Plan;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Dragos
 */
public class PlanMapper {

    @Autowired
    public PlanDao planDao;

    @Autowired
    public EventMapper eventMapper;

    public PlanDto toDto(Plan plan) {
        PlanDto dto = new PlanDto();
        dto.id = plan.getId();
        List<EventDto> listEvent = new LinkedList<>();
        for (Event event : plan.getEvents()) {
            listEvent.add(eventMapper.toDto(event));
        }
        dto.events = listEvent;
        dto.day = plan.getDay();
        return dto;
    }

    public Plan toDomain(PlanDto dto) {
        Plan plan;
        if (dto.id == null) {
            plan = new Plan();
        } else {
            plan = (Plan) planDao.getById(dto.id);
        }

        Set<Event> listEvents = new HashSet<>();
        for (EventDto eventDto : dto.events) {
            listEvents.add(eventMapper.toDomain(eventDto));
        }

        plan.setEvents(listEvents);
        plan.setDay(dto.day);

        return plan;
    }

}
