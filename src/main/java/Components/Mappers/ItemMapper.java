/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.ItemTemplateDao;
import Components.Repositories.SiteDao;
import DataToObjects.ItemClassDto;
import DataToObjects.ItemTemplateDto;
import Entities.ItemClass;
import Entities.ItemTemplate;
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
public class ItemMapper {
    
    @Autowired
    public ItemTemplateDao itemTemplateDao;
    
    @Autowired
    public SiteDao siteDao;
    
    @Autowired
    public ClassMapper classMapper;
    
    
    public ItemTemplateDto toDto(ItemTemplate item){
        ItemTemplateDto dto = new ItemTemplateDto();
        dto.id = item.getId();
        dto.hostSite = item.getHostSite().getId();
        dto.name = item.getName();
        List<ItemClassDto> dtos = new ArrayList<>();
        for(ItemClass itemClass : item.getClasses()){
            dtos.add(classMapper.toDto(itemClass));
        }
        dto.classes = dtos;
        return dto;
    }
    
    public ItemTemplate toDomain(ItemTemplateDto dto){
        ItemTemplate item;
        if(dto.id == null){
            item = new ItemTemplate();
        }else{
            item = (ItemTemplate) itemTemplateDao.getById(dto.id);
        }
        item.setName(dto.name);
        List<ItemClass> itemClasses = new ArrayList<>();
        for(ItemClassDto classDto : dto.classes){
            itemClasses.add(classMapper.toDomain(classDto));
        }
        for(ItemClass child : itemClasses){
            child.setHostItem(item);
        }
        item.setClasses(itemClasses);
        return item;
    }
    
}
