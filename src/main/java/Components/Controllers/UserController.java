/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Controllers;

import Components.Mappers.EventMapper;
import Components.Mappers.EventTypeMapper;
import Components.Mappers.PlanMapper;
import Components.Services.AccessLogService;
import Components.Services.EventService;
import Components.Services.PlanService;
import Components.Services.UserService;
import DataToObjects.DataDto;
import DataToObjects.EventTypeDto;
import DataToObjects.FilterDto;
import DataToObjects.ResponseDto;
import Entities.Event;
import Entities.EventType;
import Entities.Plan;
import Entities.User;
import Enums.Messages.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
public class UserController {

    @Autowired
    public EventService eventService;

    @Autowired
    public EventMapper eventMapper;

    @Autowired
    public UserService userService;

    @Autowired
    public PlanMapper planMapper;

    @Autowired
    public PlanService planService;

    @Autowired
    public EventTypeMapper eventTypeMapper;

    @Autowired
    public AccessLogService accessLogService;

    @RequestMapping("/user")
    public ModelAndView getUser(@RequestHeader HttpHeaders headers) {
        return new ModelAndView("user");
    }

    @RequestMapping(value = "/user/events/filter", method = RequestMethod.PUT)
    public ResponseDto getEvents(@RequestBody FilterDto dto) {
        List<Event> events = eventService.getEvents(dto.day, dto.month, dto.year, dto.start, dto.finish, dto.type);
        List<DataDto> eventList = new LinkedList<>();
        for (Event event : events) {
            eventList.add(eventMapper.toDto(event));
        }
        return new ResponseDto(Response.SUCCESFULL_GET, eventList);
    }

    @RequestMapping(value = "/user/preferences", method = RequestMethod.GET)
    public ResponseDto getPreferences(@RequestHeader HttpHeaders headers) {
        User user = userService.getByHeader(headers);
        if (accessLogService.checkAccessLog(headers)) {
            List<DataDto> list = new LinkedList<>();
            for (EventType type : user.getPreferences()) {
                list.add(eventTypeMapper.toDto(type));
            }
            return new ResponseDto(Response.SUCCESFULL_GET, list);
        }
        return new ResponseDto(Response.PERMISSION_DENIED);
    }

    @RequestMapping(value = "/user/preferences", method = RequestMethod.PUT)
    public ResponseDto setPreferences(@RequestBody List<EventTypeDto> preferences, @RequestHeader HttpHeaders headers) {
        if (accessLogService.checkAccessLog(headers)) {
            String email = headers.get("Auth-Email") != null ? headers.get("Auth-Email").get(0) : "";
            userService.updateUserPreferences(email, preferences);
            return new ResponseDto(Response.SUCCESFULL_PUT);
        }
        return new ResponseDto(Response.PERMISSION_DENIED);
    }

    @RequestMapping(value = "/user/event", method = RequestMethod.GET)
    public ResponseDto getHomeEvents(@RequestHeader HttpHeaders headers) {
        if (accessLogService.checkAccessLog(headers)) {
            User user = userService.getByHeader(headers);
            List<List<Event>> list = new LinkedList<>();
            List<Event> listForTheDay = new LinkedList<>();
            List<List<DataDto>> listDto = new LinkedList<>();
            for (EventType type : user.getPreferences()) {
                listForTheDay.addAll(eventService.getEventsToday(type.getType()));
            }
            list.addAll(eventService.groupEventsByDate(listForTheDay));
            for (List<Event> auxList : list) {
                List<DataDto> auxListDto = new ArrayList<>();
                for (Event event : auxList) {
                    auxListDto.add(eventMapper.toDto(event));
                }
                listDto.add(auxListDto);
            }
            return new ResponseDto(Response.SUCCESFULL_GET, null, listDto);
        }
        return new ResponseDto(Response.PERMISSION_DENIED);
    }

    @RequestMapping(value = "/user/plan/{id}", method = RequestMethod.GET)
    public ResponseDto getPlanById(@PathVariable Long id) {
        DataDto dto = planMapper.toDto(planService.getPlanById(id));
        return new ResponseDto(Response.SUCCESFULL_GET, Arrays.asList(dto));
    }

    @RequestMapping(value = "user/plan", method = RequestMethod.GET)
    public ResponseDto getPlanByUser(@RequestHeader HttpHeaders headers) {
        if (accessLogService.checkAccessLog(headers)) {
            List<Plan> list = planService.getPlans(headers);
            List<DataDto> listDto = new LinkedList<>();
            for (Plan plan : list) {
                listDto.add(planMapper.toDto(plan));
            }
            return new ResponseDto(Response.SUCCESFULL_GET, listDto);
        }
        return new ResponseDto(Response.PERMISSION_DENIED);
    }

    @RequestMapping(value = "user/plan/{id}", method = RequestMethod.PUT)
    public ResponseDto addToPlan(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        if (accessLogService.checkAccessLog(headers)) {
            Event event = eventService.getEventById(id);
            User user = userService.getByHeader(headers);
            Plan modifiedPlan = null;
            boolean needNewPlan = true;
            for (Plan plan : user.getPlans()) {
                if (planService.isEventFitForPlan(event, plan)) {
                    plan.getEvents().add(event);
                    needNewPlan = false;
                    modifiedPlan = plan;
                    break;
                }
            }
            if (needNewPlan) {
                Plan newPlan = new Plan();
                newPlan.setEvents(new HashSet<Event>(Arrays.asList(event)));
                newPlan.setUser(user);
                newPlan.setDay(event.getDate());
                planService.savePlan(newPlan);
            } else {
                planService.updatePlan(modifiedPlan);
            }

            return new ResponseDto(Response.SUCCESFULL_PUT);
        }
        return new ResponseDto(Response.PERMISSION_DENIED);
    }

    @RequestMapping(value = "user/plan/{id}", method = RequestMethod.DELETE)
    public ResponseDto deleteFromPlan(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        if (accessLogService.checkAccessLog(headers)) {
            Event event = eventService.getEventById(id);
            String email = headers.get("Auth-Email") != null ? headers.get("Auth-Email").get(0) : "";
            return new ResponseDto(planService.deletePlanEvents(email, event));
        }
        return new ResponseDto(Response.PERMISSION_DENIED);
    }
}
