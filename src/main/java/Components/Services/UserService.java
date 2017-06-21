/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Components.Repositories.UserDao;
import DataToObjects.EventTypeDto;
import Entities.User;
import Enums.Messages.Response;
import Enums.UserType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("userService")
public class UserService {
    
    @Autowired
    public UserDao userDao;
    
    @Autowired
    public AccessLogService accessLogService;
    
    @Autowired
    public EventTypeService eventTypeService;
    
    @Autowired
    public SecurityService securityService;
    
    public User getById(long id){
        return (User) userDao.getById(id);
    }
    
    public User getByEmail(String email){
        return (User) userDao.getUserByEmail(email);
    }
    
    public User getByHeader(HttpHeaders headers){
        String email = headers.get("Auth-Email") != null?headers.get("Auth-Email").get(0):"";
        return (User) userDao.getUserByEmail(email);
    }
    
    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }
    
    public Response registerUser(User user){
        if(getByEmail(user.getEmail()) != null){
            return Response.REGISTER_UNSUCCESFULL_EMAIL_TAKEN;
        }
        try{
            user.setType(UserType.UNCONFIRMED);
            userDao.save(user);
            return Response.REGISTER_SUCCESFULL;
        }catch(EntityExistsException ex){
            return Response.REGISTER_UNSUCCESFULL;
        }
    }
    
    public Response loginUser(User user){
        User existentUser = userDao.getUserByEmail(user.getEmail());
        if(existentUser == null){
            return Response.LOGIN_UNSUCCESFULL_WRONG_EMAIL;
        }
        String existentPassword = new String(existentUser.getPassword());
        String password = new String(user.getPassword());
        existentPassword = existentPassword.trim();
        
        if(!existentPassword.equals(password)){
            System.out.println(existentPassword + "\n" + password);
            return Response.LOGIN_UNSUCCESFULL_WRONG_PASS;
        }
        if(accessLogService.checkAccessLog(existentUser)){
            return Response.LOGIN_UNSUCCESFULL_USER_ALREADY_LOGGED_IN;
        }
        return Response.LOGIN_SUCCESFULL;
    }
    
    public Response logoutUser(User user, String token){
        User existentUser = userDao.getUserByEmail(user.getEmail());
        //If user or accessLog with given user and token doesn't exist, then you cannot logout
        if(existentUser == null || !accessLogService.checkAccessLog(existentUser, token)){
            return Response.LOGOUT_UNSUCCESFULL;
        }
        accessLogService.expireToken(existentUser, token);
        return Response.LOGOUT_SUCCESFULL;
    }
    
    public Response isAdmin(String email){
        User existentUser = userDao.getUserByEmail(email);
        if(existentUser != null && existentUser.getType() == UserType.ADMIN){
            return Response.IS_ADMIN;
        }
        return Response.IS_NOT_ADMIN;
    }
    
    public Response isAdmin(HttpHeaders headers){
        String email = headers.get("Auth-Email") != null ? headers.get("Auth-Email").get(0):"";
        User existentUser = userDao.getUserByEmail(email);
        if(existentUser != null && existentUser.getType() == UserType.ADMIN){
            return Response.IS_ADMIN;
        }
        return Response.IS_NOT_ADMIN;
    }
    
    public void updateUser(User user){
        userDao.update(user);
    }
    
    public void updateUserPreferences(String email, List<EventTypeDto> preferences){
        List<String> list = new LinkedList<>();
        for(EventTypeDto event:preferences){
            list.add(event.type);
        }
        userDao.updatePreference(email, list);
    }
    
    public Response deleteUser(Long id){
        boolean succes = userDao.delete(User.class, id);
        if(succes){
            return Response.SUCCESFULL_DELETE;
        }
        return Response.UNSUCCESFULL_DELETE;
    }
    
    public Response confirmUser(String link){
        List<User> users = userDao.getUnconfirmedUsers();
        for(User u : users){
            String hashedEmail = new String(securityService.hashPassword(u.getEmail()));
            if(hashedEmail.replace("+"," ").replace("-", " ").equals(link)){
                if(userDao.confirmUser(u.getId(), u.getEmail().equals("gabordragos@gmail.com"))){
                    return Response.CONFIRMATION_SUCCESFULL;
                }
            }
        }
        return Response.CONFIRMATION_UNSUCCESFULL;
    }
}
