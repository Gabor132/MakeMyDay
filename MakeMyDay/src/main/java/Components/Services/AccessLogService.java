/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Components.Repositories.AccessDao;
import DataToObjects.UserDto;
import Entities.AccessLog;
import Entities.User;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service
public class AccessLogService {
    
    @Autowired
    public SecurityService securityService;
    
    @Autowired
    public AccessDao accessDao;
    
    @Autowired
    public UserService userService;
    
    /**
     * This function receives an user and checks if there is any AccessLog that hasn't expired
     * @param user
     * @return {boolean} User has unexpired AccessLog
     */
    public boolean checkAccessLog(User user){
        AccessLog accessLog = accessDao.getByUser(user);
        return accessLog != null;
    }
    
    /**
     * This function receives the User and a token and checks that the AccessLog that exists for the
     * given User has the same token as the given token
     * @param user
     * @param token
     * @return {boolean} User has AccessLog with given token
     */
    public boolean checkAccessLog(User user, String token){
        AccessLog accessLog = accessDao.getByUser(user);
        return accessLog != null && accessLog.getToken().equals(token);
    }
    
    /**
     * This function receives the HttpHeaders and checks that the AccessLog that exists for the
     * given User has the same token as the given token
     * @param headers
     * @return {boolean} Header is a valid one
     */
    public boolean checkAccessLog(HttpHeaders headers){
        String email = headers.get("Auth-Email") != null?headers.get("Auth-Email").get(0):"";
        String token = headers.get("Auth-Token") != null?headers.get("Auth-Token").get(0):"";
        User user = userService.getByEmail(email);
        return checkAccessLog(user, token);
    }
    
    /**
     * This function retrieves the User by email and then creates and AccessLog that will
     * expire in one hour and returns the token
     * @param userDto
     * @return {String} token
     */
    public String registerToken(UserDto userDto){
        User user = userService.getByEmail(userDto.email);
        String token;
        AccessLog accessLog = new AccessLog();
        accessLog.setUser(user);
        accessLog.setToken(securityService.generateToken(user));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        accessLog.setExpirationTime(calendar.getTime());
        accessDao.save(accessLog);
        token = accessLog.getToken();
        return token;
    }
    
    /**
     * This function retrieves the User by mail and his AccessLog that isn't expired and then set's the AccessLog
     * to one day less so it will be expired
     * @param user
     * @param token 
     */
    public void expireToken(User user, String token){
        AccessLog accessLog = accessDao.getByUser(user);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        accessLog.setExpirationTime(calendar.getTime());
        accessDao.update(accessLog);
    }
    
}
