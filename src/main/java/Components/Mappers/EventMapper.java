/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.EventDao;
import Components.Repositories.SiteDao;
import Components.Services.EventTypeService;
import DataToObjects.EventDto;
import Entities.Event;
import Entities.SiteTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service
@Component
public class EventMapper {

    @Autowired
    public EventDao eventDao;

    @Autowired
    public SiteDao siteDao;

    @Autowired
    public EventTypeService eventTypeService;

    public EventDto toDto(Event event) {
        EventDto dto = new EventDto();
        dto.id = event.getId();
        dto.name = event.getName();
        if (event.getType() == null) {
            dto.type = "OTHER";
        } else {
            dto.type = event.getType().getType();
        }
        dto.address = event.getAddress();
        dto.price = event.getPrice();
        dto.description = event.getDescription();
        dto.link = event.getLink();
        dto.date = event.getDate();
        dto.imageUrl = event.getImageUrl();
        dto.hostSite = event.getHostSite().getName();
        return dto;
    }

    public Event toDomain(EventDto dto) {
        Event event;
        if (dto.id == null) {
            event = new Event();
        } else {
            event = (Event) eventDao.getById(dto.id);
        }

        event.setName(dto.name);
        event.setType(eventTypeService.getEventType(dto.type));
        event.setAddress(dto.address);
        event.setPrice(dto.price);
        event.setDescription(dto.description);
        event.setLink(dto.link);
        event.setDate(dto.date);
        event.setImageUrl(dto.imageUrl);
        event.setHostSite((SiteTemplate) siteDao.getByName(dto.hostSite));

        return event;
    }

}
