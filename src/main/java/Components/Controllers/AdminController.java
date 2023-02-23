/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Controllers;

import Entities.Event;
import Entities.User;
import Components.Mappers.EventMapper;
import Components.Mappers.SiteMapper;
import Components.Mappers.UserMapper;
import Components.Services.SiteService;
import DataToObjects.DataDto;
import DataToObjects.ResponseDto;
import DataToObjects.SiteDto;
import Entities.SiteTemplate;
import Enums.Messages.Response;
import Components.Parser.HtmlParser;
import Components.Services.AccessLogService;
import Components.Services.EventService;
import Components.Services.EventTypeService;
import Components.Services.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Dragos
 */
@RestController
public class AdminController{
    
    @Autowired
    public EventMapper eventMapper;
    
    @Autowired
    public UserMapper userMapper;
    
    @Autowired
    public SiteMapper siteMapper;
    
    @Autowired
    public SiteService siteService;
    
    @Autowired
    public EventService eventService;
    
    @Autowired
    public AccessLogService accessLogService;
    
    @Autowired
    public UserService userService;
    
    @Autowired
    public EventTypeService eventTypeService;
    
    @Autowired
    public HtmlParser htmlParser;
    
    @RequestMapping("/admin")
    public ModelAndView getAdmin(@RequestHeader HttpHeaders headers){
        return new ModelAndView("admin");
    }
    
    @RequestMapping(value = "admin/user", method = RequestMethod.GET)
    public ResponseDto getAllUsers(){
        List<DataDto> dtos = new ArrayList<>();
        for(User user : userService.getAllUsers()){
            dtos.add(userMapper.toDto(user));
        }
        return new ResponseDto(Response.SUCCESFULL_GET, dtos);
    }
    
    @RequestMapping(value = "admin/user", method = RequestMethod.DELETE)
    public ResponseDto deleteUser(@RequestBody Long id){
        return new ResponseDto(userService.deleteUser(id));
    }
    
    @RequestMapping(value = "admin/event/{id}", method = RequestMethod.GET)
    public ResponseDto getEventById(Long id){
        DataDto data = eventMapper.toDto((eventService.getEventById(id)));
        return new ResponseDto(Response.SUCCESFULL_GET, Arrays.asList(data));
    }
    
    @RequestMapping(value = "admin/event/name/{name}", method = RequestMethod.GET)
    public ResponseDto getEventByName(String name){
        DataDto data = eventMapper.toDto(eventService.getEventByName(name));
        return new ResponseDto(Response.SUCCESFULL_GET, Arrays.asList(data));
    }
    
    @RequestMapping(value = "admin/event/type/{type}", method = RequestMethod.GET)
    public ResponseDto getEventByType(String type){
        DataDto data = eventMapper.toDto(eventService.getEventByType(type));
        return new ResponseDto(Response.SUCCESFULL_GET, Arrays.asList(data));
    }
    
    @RequestMapping(value = "admin/event", method = RequestMethod.GET)
    public ResponseDto getAllEvents(){
        List<DataDto> dtos = new ArrayList<>();
        List<Event> events = eventService.getAllEvents();
        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        for(Event event : events){
            dtos.add(eventMapper.toDto(event));
        }
        return new ResponseDto(Response.SUCCESFULL_GET, dtos);
    }
    
    @RequestMapping(value = "admin/event", method = RequestMethod.DELETE)
    public ResponseDto deleteEvent(@RequestBody Long id){
        return new ResponseDto(eventService.deleteEvent(id));
    }
    
    @RequestMapping(value = "admin/site", method = RequestMethod.GET)
    public ResponseDto getAllSites(){
        List<DataDto> dtos = new ArrayList<>();
        for(SiteTemplate site : siteService.getAllSites()){
            dtos.add(siteMapper.toDto(site));
        }
        return new ResponseDto(Response.SUCCESFULL_GET, dtos);
    }
    
    @RequestMapping(value = "admin/site", method = RequestMethod.POST)
    public ResponseDto saveSite(@RequestBody SiteDto dto){
        try{
            SiteTemplate site = siteMapper.toDomain(dto);
            if(dto.id == -1 || dto.id == null){
                siteService.saveSite(site);
            }else{
                siteService.updateSite(site);
            }
        }catch(Exception e){
            Logger.getLogger(AdminController.class).trace(e);
            return new ResponseDto(false, e.toString());
        }
        return new ResponseDto(Response.SUCCESFULL_POST);
    }
    
    @RequestMapping(value = "admin/site", method = RequestMethod.DELETE)
    public ResponseDto deleteSite(@RequestBody SiteDto dto){
        siteService.deleteSite(dto.id);
        return new ResponseDto(Response.SUCCESFULL_DELETE);
    }
    
    /**
     * The request used to test the SiteTemplate of an added site
     * @param id
     * @return {ResponseDto}
     */
    @RequestMapping(value = "admin/site/test/{id}", method = RequestMethod.GET)
    public ResponseDto testSite(@PathVariable Long id){
        SiteTemplate site = siteService.getById(id);
        List<DataDto> data = new LinkedList<>();
        List<Event> events = htmlParser.parse(site);
        for(Event e : events){
            data.add(eventMapper.toDto(e));
        }
        return new ResponseDto(Response.SUCCESFULL_GET, data);
    }
    
    @RequestMapping(value = "admin/site/banana", method = RequestMethod.GET)
    public ResponseDto gatherEvents(){
        eventService.deleteAllEvents();
        htmlParser.scheduledSearch();
        return new ResponseDto(Response.STANDARD_SUCCESS);
    }
    
}
