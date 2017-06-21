/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.ItemClassDao;
import Components.Repositories.ItemTemplateDao;
import DataToObjects.ItemClassDto;
import Entities.ItemClass;
import Entities.ItemTemplate;
import Enums.ClassType;
import Enums.ValueType;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service
@Component
public class ClassMapper {
    
    @Autowired
    public ItemClassDao itemClassDao;
    
    @Autowired
    public ItemTemplateDao itemTemplateDao;
    
    public ItemClassDto toDto(ItemClass item){
        ItemClassDto dto = new ItemClassDto();
        dto.id = item.getId();
        dto.className = item.getClassName();
        dto.classType = item.getClassType().name();
        dto.valueType = item.getValueType().name();
        dto.valueLocation = item.getValueLocation();
        Set<ItemClassDto> dtos = new HashSet<>();
        for(ItemClass itemClass : item.getChildren()){
            dtos.add(toDto(itemClass));
        }
        dto.children = dtos;
        if(item.getParent() != null){
            dto.parent = item.getParent().getId();
        }
        return dto;
    }
    
    public ItemClass toDomain(ItemClassDto dto){
        ItemClass item;
        if(dto.id == null){
            item = new ItemClass();
        }else{
            item = (ItemClass) itemClassDao.getById(dto.id);
        }
        
        item.setClassName(dto.className);
        item.setClassType(ClassType.valueOf(dto.classType));
        item.setValueType(ValueType.valueOf(dto.valueType));
        item.setValueLocation(dto.valueLocation);
        Set<ItemClass> dtos = new HashSet<>();
        for(ItemClassDto aux : dto.children){
            dtos.add(toDomain(aux));
        }
        item.setChildren(dtos);
        for(ItemClass aux : item.getChildren()){
            aux.setParent(item);
        }
        return item;
    }
    
}
