/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.ItemTemplateDao;
import Components.Repositories.SiteDao;
import Components.Services.EventTypeService;
import DataToObjects.EventDto;
import DataToObjects.SiteDto;
import Entities.Event;
import Entities.SiteTemplate;
import Enums.EventUrlType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service
@Component
public class SiteMapper {
    
    @Autowired
    public SiteDao siteDao;
    
    @Autowired
    public ItemTemplateDao itemTemplateDao;
    
    @Autowired
    public ItemMapper itemMapper;
    
    @Autowired
    public EventMapper eventMapper;
    
    @Autowired
    public EventTypeService eventTypeService;
    
    public SiteDto toDto(SiteTemplate site){
        if(site == null)
            return null;
        SiteDto dto = new SiteDto();
        dto.id = site.getId();
        if(site.getItemTemplate() != null){
            dto.itemTemplate = itemMapper.toDto(site.getItemTemplate());
        }
        dto.link = site.getLink();
        dto.name = site.getName();
        dto.dateFormat = site.getDateFormat();
        dto.eventUrlType = site.getEventUrlType().name();
        dto.contentType = site.getContentType().getType();
        List<EventDto> dtos = new ArrayList<>();
        for(Event event : site.getEvents()){
            dtos.add(eventMapper.toDto(event));
        }
        dto.events = dtos;
        return dto;
    }
    
    public SiteTemplate toDomain(SiteDto dto){
        SiteTemplate site;
        if(dto.id == null || dto.id == -1){
            site = new SiteTemplate();
        }else{
            site = (SiteTemplate) siteDao.getById(dto.id);
        }
        site.setName(dto.name);
        site.setLink(dto.link);
        site.setDateFormat(dto.dateFormat);
        site.setEventUrlType(EventUrlType.valueOf(dto.eventUrlType));
        site.setContentType(eventTypeService.getEventType(dto.contentType));
        site.setItemTemplate(itemMapper.toDomain(dto.itemTemplate));
        site.getItemTemplate().setHostSite(site);
        return site;
    }
    
}
