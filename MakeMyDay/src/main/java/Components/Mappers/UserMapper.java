/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Mappers;

import Components.Repositories.PlanDao;
import Components.Repositories.UserDao;
import DataToObjects.UserDto;
import Entities.Plan;
import Entities.User;
import Enums.UserType;
import java.util.LinkedList;
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
public class UserMapper {
    
    @Autowired
    public UserDao userDao;
    
    @Autowired
    public PlanDao planDao;
    
    public UserDto toDto(User user){
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.email = user.getEmail();
        List<Long> dtos = new LinkedList<>();
        for(Plan plan : user.getPlans()){
            dtos.add(plan.getId());
        }
        dto.plans = dtos;
        dto.type = user.getType().name();
        return dto;
    }
    
    public User toDomain(UserDto dto){
        User user;
        if(dto.id == null){
            user = new User();
        }else{
            user = (User) userDao.getById(dto.id);
        }
        user.setEmail(dto.email);
        user.setPassword(dto.password.getBytes());
        List<Plan> plans = new LinkedList<>();
        if(dto.plans != null){
            for(long planId : dto.plans){
                plans.add((Plan) planDao.getById(planId));
            }
        }
        user.setPlans(plans);
        if(dto.type == null){
            user.setType(UserType.NORMAL);
        }else{
            user.setType(UserType.valueOf(dto.type));
        }
        return user;
    }
}
