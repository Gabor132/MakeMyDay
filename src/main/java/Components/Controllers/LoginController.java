/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Controllers;

import Components.Mappers.UserMapper;
import Components.Services.AccessLogService;
import Components.Services.MailService;
import Components.Services.SecurityService;
import Components.Services.UserService;
import DataToObjects.DataDto;
import DataToObjects.ResponseDto;
import DataToObjects.UserDto;
import Entities.User;
import Enums.Messages.Response;
import Enums.UserType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Dragos
 */
@RestController
public class LoginController {

    @Autowired
    public AccessLogService accessLogService;

    @Autowired
    public UserService userService;

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public SecurityService securityService;

    @Autowired
    public MailService mailService;

    @RequestMapping("/login")
    public ModelAndView getLogin(@RequestHeader HttpHeaders headers) {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "login/isAdmin", method = RequestMethod.GET)
    public ResponseDto isAdmin(@RequestHeader HttpHeaders headers) {
        return new ResponseDto(
                userService.isAdmin(headers.get("Auth-Email") != null ? headers.get("Auth-Email").get(0) : ""));
    }

    @RequestMapping(value = "login/login", method = RequestMethod.POST)
    public ResponseDto loginUser(@RequestBody UserDto userDto) {
        User user = userMapper.toDomain(userDto);
        user.setPassword(securityService.hashPassword(userDto.password));
        Response response = userService.loginUser(user);
        if (response == Response.LOGIN_SUCCESFULL) {
            DataDto data = new DataDto();
            data.token = accessLogService.registerToken(userDto);
            data.email = userDto.email;
            return new ResponseDto(response, Arrays.asList(data));
        }
        return new ResponseDto(response);
    }

    @RequestMapping(value = "login/register", method = RequestMethod.POST)
    public ResponseDto registerUser(@RequestBody UserDto userDto) {
        User user = userMapper.toDomain(userDto);
        // TODO - DELET THIS!
        if (user.getEmail().equals("gabordragos@gmail.com")) {
            user.setType(UserType.ADMIN);
        }
        user.setPassword((securityService.hashPassword(userDto.password)));
        ResponseDto response = new ResponseDto(userService.registerUser(user));
        mailService.sendConfirmation(user);
        return response;
    }

    @RequestMapping(value = "login/logout", method = RequestMethod.PUT)
    public ResponseDto logoutUser(@RequestBody DataDto accessDto) {
        User user = userService.getByEmail(accessDto.email);
        if (user == null) {
            return new ResponseDto(Response.LOGOUT_UNSUCCESFULL);
        }
        return new ResponseDto(userService.logoutUser(user, accessDto.token));
    }

    @RequestMapping(value = "login/confirmation", method = RequestMethod.GET)
    public ModelAndView confirmUser(@RequestParam("link") String link) {
        try {
            link = URLDecoder.decode(link, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseDto response = new ResponseDto(userService.confirmUser(link));
        ModelAndView model = new ModelAndView("confirmation");
        model.addObject("message", response.message);
        return model;
    }

}
